package dev.maxbogomol.paw_paint.gui.canvas;

import dev.maxbogomol.paw_paint.core.canvas.Canvas;
import dev.maxbogomol.paw_paint.core.color.Color;
import dev.maxbogomol.paw_paint.core.color.Colors;
import org.freedesktop.cairo.*;
import org.freedesktop.cairo.Filter;
import org.gnome.gdk.ModifierType;
import org.gnome.gtk.*;

import java.io.IOException;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.util.Set;

public class CanvasArea {
    private static final double MIN_ZOOM = 0.01f;
    private static final double MAX_ZOOM = 256.0f;
    private static final double ROTATION_ANGLE_LOCK = Math.toRadians(15.0f);
    private static final double MOUSE_SCROLL_ANGLE_STEP = Math.toRadians(5.0f);
    private static final int BACKGROUND_PATTERN_CELL_SIZE = 32;

    private final Canvas canvas;

    private final Box mainWidgetBox;

    private final DrawingArea drawingArea;
    private ImageSurface imageSurface;
    private byte[] pixelData;
    private Color color = Color.create();

    private SurfacePattern imagePattern;
    private SurfacePattern backgroundPattern;

    private double canvasOffsetX = 0;
    private double canvasOffsetY = 0;
    private double canvasZoom = 1f;
    private double canvasRotation = 0;

    private double drawingAreaDragStartX = 0;
    private double drawingAreaDragStartY = 0;
    private double drawingAreaDragOffsetX = 0;
    private double drawingAreaDragOffsetY = 0;
    private double drawingAreaMouseX = 0;
    private double drawingAreaMouseY = 0;
    private double drawingAreaDragStartAngle = 0;
    private double drawingAreaDragRotation = 0;
    private boolean drawingAreaDragRotating = false;

    public CanvasArea(Canvas canvas) {
        this.canvas = canvas;
        this.pixelData = canvas.getColorFormat().createPixelData();

        drawingArea = new DrawingArea();
        drawingArea.setContentWidth(canvas.getWidth());
        drawingArea.setContentHeight(canvas.getHeight());
        drawingArea.setHexpand(true);
        drawingArea.setVexpand(true);

        drawingArea.setDrawFunc(this::setDrawingAreaDrawFunction);

        GestureDrag gestureDrag = new GestureDrag();
        gestureDrag.setButton(2);
        gestureDrag.onDragBegin((startX, startY) -> onDrawingAreaDragBegin(gestureDrag, startX, startY));
        gestureDrag.onDragUpdate((offsetX, offsetY) -> onDrawingAreaDragUpdate(gestureDrag, offsetX, offsetY));
        gestureDrag.onDragEnd((offsetX, offsetY) -> onDrawingAreaDragEnd(gestureDrag, offsetX, offsetY));

        EventControllerMotion motionController = new EventControllerMotion();
        motionController.onMotion((x, y) -> onDrawingAreaMotion(motionController, x, y));

        EventControllerScroll scrollController = new EventControllerScroll(EventControllerScrollFlags.VERTICAL);
        scrollController.onScroll((dx, dy) -> onDrawingAreaScroll(scrollController, dx, dy));

        drawingArea.addController(gestureDrag);
        drawingArea.addController(motionController);
        drawingArea.addController(scrollController);

        mainWidgetBox = new Box(Orientation.VERTICAL, 0);
        mainWidgetBox.append(drawingArea);

        createSurface();
        createBackgroundPattern();

        blendSurface();
    }

    private void createSurface() {
        boolean intSurface = canvas.getColorFormat().getBitDepth() <= 8;
        imageSurface = ImageSurface.create(intSurface ? Format.ARGB32 : Format.RGBA128F, canvas.getWidth(), canvas.getHeight());

        imagePattern = SurfacePattern.create(imageSurface);
        imagePattern.setExtend(Extend.REPEAT);
    }

    private void createBackgroundPattern() {
        int cellSize = BACKGROUND_PATTERN_CELL_SIZE;
        int patternSize = cellSize * 2;

        ImageSurface patternSurface = ImageSurface.create(Format.RGB24, patternSize, patternSize);
        Context cr = null;
        try {
            cr = Context.create(patternSurface);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        cr.setSourceRGB(Colors.WHITE.getRed(), Colors.WHITE.getGreen(), Colors.WHITE.getBlue());
        cr.rectangle(0, 0, cellSize, cellSize);
        cr.rectangle(cellSize, cellSize, cellSize, cellSize);
        cr.fill();

        cr.setSourceRGB(Colors.LIGHT_GRAY.getRed(), Colors.LIGHT_GRAY.getGreen(), Colors.LIGHT_GRAY.getBlue());
        cr.rectangle(cellSize, 0, cellSize, cellSize);
        cr.rectangle(0, cellSize, cellSize, cellSize);
        cr.fill();

        backgroundPattern = SurfacePattern.create(patternSurface);
        backgroundPattern.setExtend(Extend.REPEAT);
        backgroundPattern.setFilter(Filter.NEAREST);
    }

    public Box getMainWidgetBox() {
        return mainWidgetBox;
    }

    private void blendSurface() {
        boolean intSurface = canvas.getColorFormat().getBitDepth() <= 8;

        MemorySegment data = imageSurface.getData();
        int stride = imageSurface.getStride();
        int channelValue = 255;

        for (int y = 0; y < canvas.getHeight(); y++) {
            for (int x = 0; x < canvas.getWidth(); x++) {
                canvas.getMainLayerGroup().getPixel(x, y, color, pixelData);

                float red = color.r;
                float green = color.g;
                float blue = color.b;
                float alpha = color.a;

                if (intSurface) {
                    int argb = 0;

                    if (alpha > 0) {
                        float mult = alpha * channelValue;
                        int r = (int) (red * mult);
                        int g = (int) (green * mult);
                        int b = (int) (blue * mult);
                        int a = (int) (mult);

                        argb = (a << 24) | (r << 16) | (g << 8) | b;
                    }

                    long pixelOffset = (long) y * stride + x * 4L;
                    data.set(ValueLayout.JAVA_INT, pixelOffset, argb);
                } else {
                    if (alpha == 0) red = green = blue = alpha = 0;

                    long pixelOffset = (long) y * stride + x * 16L;
                    data.set(ValueLayout.JAVA_FLOAT, pixelOffset, red);
                    data.set(ValueLayout.JAVA_FLOAT, pixelOffset + 4, green);
                    data.set(ValueLayout.JAVA_FLOAT, pixelOffset + 8, blue);
                    data.set(ValueLayout.JAVA_FLOAT, pixelOffset + 12, alpha);
                }
            }
        }
        imageSurface.markDirty();
    }

    private void setDrawingAreaDrawFunction(DrawingArea drawingArea, Context cr, int width, int height) {
        double centerX = width / 2f;
        double centerY = height / 2f;
        
        cr.translate(centerX + canvasOffsetX, centerY + canvasOffsetY);
        cr.rotate(canvasRotation);
        cr.scale(canvasZoom, canvasZoom);
        cr.translate(-canvas.getWidth() / 2f, -canvas.getHeight() / 2f);

        cr.save();
        cr.rectangle(0, 0, canvas.getWidth(), canvas.getHeight());
        cr.clip();
        cr.identityMatrix();
        cr.setSource(backgroundPattern);
        cr.paint();
        cr.restore();

        if (true) {
            cr.setSource(imageSurface, 0, 0);
            cr.getSource().setFilter(Filter.NEAREST);
            cr.paint();
        } else {
            cr.save();
            cr.setSource(imagePattern);
            cr.getSource().setFilter(Filter.NEAREST);
            cr.paint();
            cr.restore();
        }
    }

    private void onDrawingAreaDragBegin(GestureDrag gestureDrag, double startX, double startY) {
        int button = gestureDrag.getCurrentButton();
        if (button == 2) {
            drawingAreaDragStartX = startX;
            drawingAreaDragStartY = startY;
            drawingAreaDragOffsetX = canvasOffsetX;
            drawingAreaDragOffsetY = canvasOffsetY;

            Set<ModifierType> modifiers = gestureDrag.getCurrentEventState();
            if (modifiers.contains(ModifierType.SHIFT_MASK)) {
                drawingAreaDragRotating = true;
                drawingAreaDragRotation = canvasRotation;

                double centerX = drawingArea.getWidth() / 2.0;
                double centerY = drawingArea.getHeight() / 2.0;

                drawingAreaDragStartAngle = Math.atan2(startY - centerY, startX - centerX);
            } else {
                drawingAreaDragRotating = false;
            }
        }
    }

    private void onDrawingAreaDragUpdate(GestureDrag gestureDrag, double offsetX, double offsetY) {
        int button = gestureDrag.getCurrentButton();
        if (button == 2) {
            if (drawingAreaDragRotating) {
                Set<ModifierType> modifiers = gestureDrag.getCurrentEventState();
                boolean lockRotation = modifiers.contains(ModifierType.CONTROL_MASK);
                boolean centerPivot = modifiers.contains(ModifierType.ALT_MASK);

                double centerX = drawingArea.getWidth() / 2.0f;
                double centerY = drawingArea.getHeight() / 2.0f;
                double currentX = drawingAreaDragStartX + offsetX;
                double currentY = drawingAreaDragStartY + offsetY;

                double currentAngle = Math.atan2(currentY - centerY, currentX - centerX);
                double newRotation = drawingAreaDragRotation + (currentAngle - drawingAreaDragStartAngle);
                if (lockRotation) newRotation = Math.round(newRotation / ROTATION_ANGLE_LOCK) * ROTATION_ANGLE_LOCK;

                double deltaAngle = newRotation - drawingAreaDragRotation;

                double pivotX = centerPivot ? centerX : drawingAreaDragStartX;
                double pivotY = centerPivot ? centerY : drawingAreaDragStartY;
                double canvasCenterX = centerX + drawingAreaDragOffsetX;
                double canvasCenterY = centerY + drawingAreaDragOffsetY;
                double relativeX = canvasCenterX - pivotX;
                double relativeY = canvasCenterY - pivotY;

                double rotatedCenterX = pivotX + (relativeX * Math.cos(deltaAngle) - relativeY * Math.sin(deltaAngle));
                double rotatedCenterY = pivotY + (relativeX * Math.sin(deltaAngle) + relativeY * Math.cos(deltaAngle));

                canvasOffsetX = rotatedCenterX - centerX;
                canvasOffsetY = rotatedCenterY - centerY;

                canvasRotation = newRotation;
            } else {
                canvasOffsetX = drawingAreaDragOffsetX + offsetX;
                canvasOffsetY = drawingAreaDragOffsetY + offsetY;
            }
            drawingArea.queueDraw();
        }
    }

    private void onDrawingAreaDragEnd(GestureDrag gestureDrag, double offsetX, double offsetY) {
        drawingAreaDragRotating = false;
    }

    private void onDrawingAreaMotion(EventControllerMotion motionController, double x, double y) {
        drawingAreaMouseX = x;
        drawingAreaMouseY = y;
    }

    private boolean onDrawingAreaScroll(EventControllerScroll scrollController, double dx, double dy) {
        Set<ModifierType> modifiers = scrollController.getCurrentEventState();
        if (!drawingAreaDragRotating && !modifiers.contains(ModifierType.BUTTON2_MASK)) {
            double centerX = drawingArea.getWidth() / 2.0f;
            double centerY = drawingArea.getHeight() / 2.0f;

            if (modifiers.contains(ModifierType.SHIFT_MASK)) {
                boolean lockRotation = modifiers.contains(ModifierType.CONTROL_MASK);
                boolean centerPivot = modifiers.contains(ModifierType.ALT_MASK);

                double deltaAngle = (lockRotation ? ROTATION_ANGLE_LOCK : MOUSE_SCROLL_ANGLE_STEP) * dy;

                double pivotX = centerPivot ? centerX : drawingAreaMouseX;
                double pivotY = centerPivot ? centerY : drawingAreaMouseY;
                double relativeX = pivotX - (centerX + canvasOffsetX);
                double relativeY = pivotY - (centerY + canvasOffsetY);

                canvasOffsetX += relativeX - (relativeX * Math.cos(deltaAngle) - relativeY * Math.sin(deltaAngle));
                canvasOffsetY += relativeY - (relativeX * Math.sin(deltaAngle) + relativeY * Math.cos(deltaAngle));

                canvasRotation += deltaAngle;
                if (lockRotation) canvasRotation = Math.round(canvasRotation / ROTATION_ANGLE_LOCK) * ROTATION_ANGLE_LOCK;

                drawingArea.queueDraw();
            } else {
                double zoomFactor = Math.pow(1.1, -dy);
                double newZoom = Math.clamp(canvasZoom * zoomFactor, MIN_ZOOM, MAX_ZOOM);

                if (newZoom != canvasZoom) {
                    double scaleRatio = newZoom / canvasZoom;

                    double relativeX = drawingAreaMouseX - (centerX + canvasOffsetX);
                    double relativeY = drawingAreaMouseY - (centerY + canvasOffsetY);

                    canvasOffsetX -= relativeX * (scaleRatio - 1.0f);
                    canvasOffsetY -= relativeY * (scaleRatio - 1.0f);

                    canvasZoom = newZoom;
                    
                    drawingArea.queueDraw();
                }
            }
        }
        return true;
    }
}

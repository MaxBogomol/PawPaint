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
    private static final double ZOOM_CLAMP_OFFSET = 128.0f;
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

    private double mouseX = 0;
    private double mouseY = 0;
    private double dragStartX = 0;
    private double dragStartY = 0;
    private double dragOffsetX = 0;
    private double dragOffsetY = 0;
    private double dragStartAngle = 0;
    private double dragRotation = 0;
    private boolean dragRotating = false;

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
        gestureDrag.onDragBegin((startX, startY) -> onCanvasDragBegin(gestureDrag, startX, startY));
        gestureDrag.onDragUpdate((offsetX, offsetY) -> onCanvasAreaDragUpdate(gestureDrag, offsetX, offsetY));
        gestureDrag.onDragEnd((offsetX, offsetY) -> onCanvasDragEnd(gestureDrag, offsetX, offsetY));

        EventControllerMotion motionController = new EventControllerMotion();
        motionController.onMotion((x, y) -> onCanvasMotion(motionController, x, y));

        EventControllerScroll scrollController = new EventControllerScroll(EventControllerScrollFlags.VERTICAL);
        scrollController.onScroll((dx, dy) -> onCanvasScroll(scrollController, dx, dy));

        drawingArea.addController(gestureDrag);
        drawingArea.addController(motionController);
        drawingArea.addController(scrollController);

        mainWidgetBox = new Box(Orientation.VERTICAL, 0);
        mainWidgetBox.append(drawingArea);

        createImageSurface();
        createBackgroundPattern();

        blendSurface();
    }

    private void createImageSurface() {
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

    private void clampCanvasOffset() {
        double baseMaxOffsetX = Math.max(0.0f, (drawingArea.getWidth() / 2.0f) - ZOOM_CLAMP_OFFSET);
        double baseMaxOffsetY = Math.max(0.0f, (drawingArea.getHeight() / 2.0f) - ZOOM_CLAMP_OFFSET);

        double zoomScale = canvasZoom / MIN_ZOOM;
        double maxOffsetX = baseMaxOffsetX * zoomScale;
        double maxOffsetY = baseMaxOffsetY * zoomScale;

        canvasOffsetX = Math.clamp(canvasOffsetX, -maxOffsetX, maxOffsetX);
        canvasOffsetY = Math.clamp(canvasOffsetY, -maxOffsetY, maxOffsetY);
    }

    private void updateDrawingArea() {
        drawingArea.queueDraw();
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

    private void onCanvasDragBegin(GestureDrag gestureDrag, double startX, double startY) {
        int button = gestureDrag.getCurrentButton();
        if (button == 2) {
            dragStartX = startX;
            dragStartY = startY;
            dragOffsetX = canvasOffsetX;
            dragOffsetY = canvasOffsetY;

            Set<ModifierType> modifiers = gestureDrag.getCurrentEventState();
            if (modifiers.contains(ModifierType.SHIFT_MASK)) {
                dragRotating = true;
                dragRotation = canvasRotation;

                double centerX = drawingArea.getWidth() / 2.0f;
                double centerY = drawingArea.getHeight() / 2.0f;

                dragStartAngle = Math.atan2(startY - centerY, startX - centerX);
            } else {
                dragRotating = false;
            }
        }
    }

    private void onCanvasAreaDragUpdate(GestureDrag gestureDrag, double offsetX, double offsetY) {
        int button = gestureDrag.getCurrentButton();
        if (button == 2) {
            if (dragRotating) {
                Set<ModifierType> modifiers = gestureDrag.getCurrentEventState();
                boolean lockRotation = modifiers.contains(ModifierType.CONTROL_MASK);
                boolean centerPivot = modifiers.contains(ModifierType.ALT_MASK);

                double centerX = drawingArea.getWidth() / 2.0f;
                double centerY = drawingArea.getHeight() / 2.0f;
                double currentX = dragStartX + offsetX;
                double currentY = dragStartY + offsetY;

                double currentAngle = Math.atan2(currentY - centerY, currentX - centerX);
                double newRotation = dragRotation + (currentAngle - dragStartAngle);
                if (lockRotation) newRotation = Math.round(newRotation / ROTATION_ANGLE_LOCK) * ROTATION_ANGLE_LOCK;

                double deltaAngle = newRotation - dragRotation;

                double pivotX = centerPivot ? centerX : dragStartX;
                double pivotY = centerPivot ? centerY : dragStartY;
                double canvasCenterX = centerX + dragOffsetX;
                double canvasCenterY = centerY + dragOffsetY;
                double relativeX = canvasCenterX - pivotX;
                double relativeY = canvasCenterY - pivotY;

                double rotatedCenterX = pivotX + (relativeX * Math.cos(deltaAngle) - relativeY * Math.sin(deltaAngle));
                double rotatedCenterY = pivotY + (relativeX * Math.sin(deltaAngle) + relativeY * Math.cos(deltaAngle));

                canvasOffsetX = rotatedCenterX - centerX;
                canvasOffsetY = rotatedCenterY - centerY;
                clampCanvasOffset();

                canvasRotation = newRotation;
            } else {
                canvasOffsetX = dragOffsetX + offsetX;
                canvasOffsetY = dragOffsetY + offsetY;
                clampCanvasOffset();
            }
            updateDrawingArea();
        }
    }

    private void onCanvasDragEnd(GestureDrag gestureDrag, double offsetX, double offsetY) {
        dragRotating = false;
    }

    private void onCanvasMotion(EventControllerMotion motionController, double x, double y) {
        mouseX = x;
        mouseY = y;
    }

    private boolean onCanvasScroll(EventControllerScroll scrollController, double dx, double dy) {
        Set<ModifierType> modifiers = scrollController.getCurrentEventState();
        if (!dragRotating && !modifiers.contains(ModifierType.BUTTON2_MASK)) {
            double centerX = drawingArea.getWidth() / 2.0f;
            double centerY = drawingArea.getHeight() / 2.0f;

            boolean centerPivot = modifiers.contains(ModifierType.ALT_MASK);

            if (modifiers.contains(ModifierType.SHIFT_MASK)) {
                boolean lockRotation = modifiers.contains(ModifierType.CONTROL_MASK);

                double deltaAngle = (lockRotation ? ROTATION_ANGLE_LOCK : MOUSE_SCROLL_ANGLE_STEP) * dy;

                double pivotX = centerPivot ? centerX : mouseX;
                double pivotY = centerPivot ? centerY : mouseY;
                double relativeX = pivotX - (centerX + canvasOffsetX);
                double relativeY = pivotY - (centerY + canvasOffsetY);

                canvasOffsetX += relativeX - (relativeX * Math.cos(deltaAngle) - relativeY * Math.sin(deltaAngle));
                canvasOffsetY += relativeY - (relativeX * Math.sin(deltaAngle) + relativeY * Math.cos(deltaAngle));
                clampCanvasOffset();

                canvasRotation += deltaAngle;
                if (lockRotation) canvasRotation = Math.round(canvasRotation / ROTATION_ANGLE_LOCK) * ROTATION_ANGLE_LOCK;

                updateDrawingArea();
            } else {
                double zoomFactor = Math.pow(1.1f, -dy);
                double newZoom = Math.clamp(canvasZoom * zoomFactor, MIN_ZOOM, MAX_ZOOM);

                if (newZoom != canvasZoom) {
                    double scaleRatio = newZoom / canvasZoom;

                    double pivotX = centerPivot ? centerX : mouseX;
                    double pivotY = centerPivot ? centerY : mouseY;
                    double relativeX = pivotX - (centerX + canvasOffsetX);
                    double relativeY = pivotY - (centerY + canvasOffsetY);

                    canvasOffsetX -= relativeX * (scaleRatio - 1.0f);
                    canvasOffsetY -= relativeY * (scaleRatio - 1.0f);

                    canvasZoom = newZoom;

                    clampCanvasOffset();
                    updateDrawingArea();
                }
            }
        }
        return true;
    }
}

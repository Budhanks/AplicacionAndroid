package com.example.aplicacion;

import android.content.Context;
import android.graphics.Bitmap; // Importa la clase Bitmap
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF; // Importa RectF para el posicionamiento de la imagen
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class DrawView extends View {

    // Clase interna para almacenar cada trazo con su color específico
    private static class Stroke {
        Path path;  // La geometría del trazo
        int color;  // El color con el que se dibujó este trazo

        Stroke(Path path, int color) {
            this.path = path;
            this.color = color;
        }
    }

    private List<Stroke> strokes; // Lista para guardar todos los trazos dibujados
    private Paint drawingPaint;   // Objeto Paint para configurar el estilo de dibujo (grosor, etc.)
    private Path currentPath;     // El Path para el trazo que se está dibujando actualmente
    private int currentDrawingColor; // El color actualmente seleccionado para los nuevos trazos

    private Bitmap loadedImage; // La imagen cargada para dibujar
    private RectF imageRect;    // Rectángulo que define dónde se dibujará la imagen

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        strokes = new ArrayList<>(); // Inicializa la lista de trazos

        drawingPaint = new Paint();
        drawingPaint.setStyle(Paint.Style.STROKE); // Estilo de trazo (solo contorno)
        drawingPaint.setStrokeWidth(10); // Grosor del trazo
        drawingPaint.setAntiAlias(true); // Suaviza los bordes de los trazos

        currentDrawingColor = Color.BLACK; // Color inicial por defecto
    }

    /**
     * Establece el color para los NUEVOS trazos.
     * Los trazos ya dibujados no cambian de color.
     * @param color El nuevo color (ej. Color.RED, Color.BLUE).
     */
    public void setPaintColor(int color) {
        currentDrawingColor = color;
    }

    /**
     * Borra todos los trazos en la pantalla.
     * Limpia la lista de trazos y el trazo actual, luego solicita un redibujado.
     */
    public void clearDrawing() {
        strokes.clear();     // Elimina todos los trazos guardados
        currentPath = null;  // Asegura que no haya un trazo actual pendiente
        invalidate();        // Solicita un redibujado de la vista para mostrar el lienzo vacío
    }

    /**
     * Establece la imagen a dibujar en el fondo del lienzo.
     * La imagen se escalará para ajustarse al ancho de la vista manteniendo su relación de aspecto,
     * y se centrará verticalmente.
     * @param bitmap La imagen Bitmap a cargar.
     */
    public void setLoadedImage(Bitmap bitmap) {
        this.loadedImage = bitmap;
        if (loadedImage != null) {
            // Calcula el RectF para dibujar la imagen, escalándola para que se ajuste al ancho de la vista
            // y manteniendo su relación de aspecto.
            int viewWidth = getWidth();
            int viewHeight = getHeight();
            if (viewWidth == 0 || viewHeight == 0) {
                // Si la vista aún no tiene dimensiones, esperamos a onSizeChanged
                // o a que se establezcan las dimensiones más tarde.
                // Por ahora, solo marcamos que la imagen está lista para ser calculada.
                imageRect = null; // Reiniciamos para que se recalcule en onSizeChanged
            } else {
                calculateImageRect(viewWidth, viewHeight);
            }
        } else {
            imageRect = null;
        }
        invalidate(); // Solicita un redibujado para mostrar la nueva imagen
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Recalcular el tamaño y posición de la imagen si la vista cambia de tamaño
        if (loadedImage != null) {
            calculateImageRect(w, h);
        }
    }

    // Método auxiliar para calcular el RectF de la imagen
    private void calculateImageRect(int viewWidth, int viewHeight) {
        if (loadedImage == null || viewWidth == 0 || viewHeight == 0) {
            imageRect = null;
            return;
        }

        float imageAspect = (float) loadedImage.getWidth() / loadedImage.getHeight();
        float viewAspect = (float) viewWidth / viewHeight;

        float drawWidth;
        float drawHeight;

        if (imageAspect > viewAspect) {
            // La imagen es más ancha que la vista, ajusta al ancho de la vista
            drawWidth = viewWidth;
            drawHeight = viewWidth / imageAspect;
        } else {
            // La imagen es más alta que la vista, ajusta a la altura de la vista
            drawHeight = viewHeight;
            drawWidth = viewHeight * imageAspect;
        }

        // Centrar la imagen
        float left = (viewWidth - drawWidth) / 2;
        float top = (viewHeight - drawHeight) / 2;
        imageRect = new RectF(left, top, left + drawWidth, top + drawHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        // Dibuja la imagen primero si está cargada
        if (loadedImage != null && imageRect != null) {
            canvas.drawBitmap(loadedImage, null, imageRect, null);
        }

        // Dibuja cada trazo almacenado en la lista con su color original
        for (Stroke stroke : strokes) {
            drawingPaint.setColor(stroke.color); // Establece el color específico para este trazo
            canvas.drawPath(stroke.path, drawingPaint); // Dibuja el trazo
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Cuando el usuario toca la pantalla, inicia un nuevo trazo.
                currentPath = new Path(); // Crea un nuevo Path para este trazo
                currentPath.moveTo(x, y); // Mueve el punto de inicio a la posición actual
                // Añade el nuevo trazo a la lista con el color actual de dibujo
                strokes.add(new Stroke(currentPath, currentDrawingColor));
                break;
            case MotionEvent.ACTION_MOVE:
                // Mientras el usuario arrastra el dedo, extiende el trazo actual.
                if (currentPath != null) { // Asegura que hay un trazo actual
                    currentPath.lineTo(x, y); // Añade un segmento de línea al Path actual
                }
                break;
            case MotionEvent.ACTION_UP:
                // Cuando el usuario levanta el dedo, el trazo actual ha terminado.
                currentPath = null; // Resetea currentPath para indicar que no hay un trazo activo
                break;
        }

        invalidate(); // Solicita un redibujado de la vista para mostrar el trazo actualizado
        return true; // Indica que hemos manejado este evento
    }
}
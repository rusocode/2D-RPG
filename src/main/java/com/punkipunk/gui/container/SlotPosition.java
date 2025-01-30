package com.punkipunk.gui.container;

/**
 * <p>
 * Los records son una caracteristica introducida en Java 16 (preview en Java 14) que proporciona una forma concisa de declarar
 * clases inmutables que actuan principalmente como portadores de datos.
 * <p>
 * Caracteristicas principales:
 * <pre>{@code
 * // Forma tradicional sin records
 * public class Point {
 *     private final int x;
 *     private final int y;
 *
 *     public Point(int x, int y) {
 *         this.x = x;
 *         this.y = y;
 *     }
 *
 *     public int getX() { return x; }
 *     public int getY() { return y; }
 *
 *     @Override
 *     public boolean equals(Object o) {
 *         if (this == o) return true;
 *         if (o == null || getClass() != o.getClass()) return false;
 *         Point point = (Point) o;
 *         return x == point.x && y == point.y;
 *     }
 *
 *     @Override
 *     public int hashCode() {
 *         return Objects.hash(x, y);
 *     }
 *
 *     @Override
 *     public String toString() {
 *         return "Point[x=" + x + ", y=" + y + "]";
 *     }
 * }
 *
 * // La misma clase usando record
 * public record Point(int x, int y) {}
 * }</pre>
 * <p>
 * Beneficios principales:
 * <ol>
 * <li><b>Inmutabilidad automatica</b>
 * <ul>
 * <li>Todos los campos son automaticamente final
 * <li>No se pueden modificar despues de la creacion
 * </ul>
 * <li><b>Metodos generados automaticamente</b>
 * <ul>
 * <li>Constructor
 * <li>Getters (usando nombres de campos)
 * <li>equals() y hashCode()
 * <li>toString()
 * </ul>
 * <li><b>Uso en colecciones</b>
 * <pre>{@code
 * // Son excelentes como claves en Maps
 * Map<Point, String> locationDescriptions = new HashMap<>();
 * locationDescriptions.put(new Point(0, 0), "Origin");
 * }</pre>
 * <li><b>Constructores compactos</b>
 * <pre>{@code
 * public record Point(int x, int y) {
 *     // Validacion en constructor compacto
 *     public Point {
 *         if (x < 0 || y < 0) {
 *             throw new IllegalArgumentException("Coordinates cannot be negative");
 *         }
 *     }
 * }
 * }</pre>
 * <li><b>Metodos adicionales</b>
 * <pre>{@code
 * public record Point(int x, int y) {
 *     // Puedes agregar metodos
 *     public double distanceFromOrigin() {
 *         return Math.sqrt(x * x + y * y);
 *     }
 * }
 * }</pre>
 * <li><b>Deconstruccion</b>
 * <pre>{@code
 * Point point = new Point(10, 20);
 * int x = point.x();  // En lugar de getX()
 * int y = point.y();  // En lugar de getY()
 * }</pre>
 * </ol>
 * Casos de uso ideal en mensajes o eventos:
 * <pre>{@code
 * public record GameEvent(
 *     String eventType,
 *     long timestamp,
 *     Map<String, Object> data
 * ) {}
 * }</pre>
 */

public record SlotPosition(int row, int col) {
}

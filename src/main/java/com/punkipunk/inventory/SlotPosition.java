package com.punkipunk.inventory;

/**
 * <p>
 * Los Record en Java (introducidos en Java 14) son un tipo especial de clase diseñada especificamente para almacenar datos
 * inmutables de manera concisa. La clase SlotPosition es un candidato perfecto para ser un record porque:
 * <ol>
 * <li>Solo tiene campos finales (inmutables)
 * <li>Su propocito principal es almacenar datos (row,col)
 * <li>Tienes implementados equals() y hashCode()</li>
 * <li>Tienes getters para acceder a los datos
 * </ol>
 * <p>
 * Por la tanto se podria simplificar a:
 * <pre>{@code
 * public record SlotPosition(int row, int col) { }
 * }</pre>
 * <p>
 * Este simple record automaticamente te proporciona:
 * <ul>
 * <li>Constructor con todos los campos
 * <li>Metodos getters (aunque se llamen sin 'get', directamente row() y col())
 * <li>equals() y hashCode()
 * <li>Los campos finales por defecto
 * <li>Es inmutable
 * </ul>
 * <p>
 * Las ventajas de usar records son:
 * <ol>
 * <li>Menos codigo boilerplate (codigo repetitivo)
 * <li>Menos probabilidad de errores al no tener que implementar manualmente equals/hashCode
 * <li>Intencion mas clara del proposito de la clase (transportar datos)
 * <li>Inmutabilidad garantizada
 * </ol>
 * <p>
 * El unico "inconveniente" es que si necesitas añadir logica adicional o validaciones, tendrias que hacerlo de forma explicita,
 * pero aun asi puedes hacerlo dentro del record:
 * <pre>{@code
 * public record SlotPosition(int row, int col) {
 *     // Constructor compacto para validaciones
 *     public SlotPosition {
 *         if (row < 0 || col < 0) {
 *             throw new IllegalArgumentException("Row and col must be positive");
 *         }
 *     }
 *
 *     // Metodos adicionales si los necesitas
 *     public boolean isValid() {
 *         return row >= 0 && col >= 0;
 *     }
 * }
 * }</pre>
 */

public record SlotPosition(int row, int col) {

}

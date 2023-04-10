package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.World;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Crear un texture atlas con todos los items.
 */

public class Item extends Entity {

	protected int value;
	/* Se utiliza la estructura de datos de tipo Map para almacenar instancias de tipo Item. Esto se hace utilizando la
	 * interfaz Supplier como proveedor de valor para crear una instancia de tipo Item usando la expresion lambda. La
	 * expresion () -> new Axe(game) implementa el metodo get() de la interfaz Supplier para devolver una nueva
	 * instancia de Item cada vez que se llama. La clave en el map, es un objeto de tipo Class que es una subclase de la
	 * clase Item. La sintaxis <? extends Item> indica que la clave puede ser cualquier subclase de Item. */
	public Map<Class<? extends Item>, Supplier<Item>> itemMap;

	public Item(Game game, World world) {
		super(game, world);
		itemMap = new HashMap<>();
		itemMap.put(Axe.class, () -> new Axe(game, world));
		itemMap.put(Key.class, () -> new Key(game, world));
		itemMap.put(PotionRed.class, () -> new PotionRed(game, world));
		itemMap.put(ShieldBlue.class, () -> new ShieldBlue(game, world));
		itemMap.put(ShieldWood.class, () -> new ShieldWood(game, world));
		itemMap.put(SwordNormal.class, () -> new SwordNormal(game, world));
	}

}

package org.restaurant.runner;

/*
Создать таблицу «Меню в ресторане». Колонки: название блюда,
его стоимость, вес, наличие скидки. Написать код для
добавления записей в таблицу и их выборки по критериям
«стоимость от-до», «только со скидкой», выбрать набор блюд
так, чтобы их суммарный вес был не более 1 КГ.
 */

import org.restaurant.entity.Menu;
import org.restaurant.util.MenuDbUtil;

import java.util.ArrayList;
import java.util.List;

public class Runner {
    public static void main(String[] args) {

        MenuDbUtil db = new MenuDbUtil();
        List<Menu> menu = new ArrayList<Menu>();
        db.addDish(new Menu("dish-1", 50.0, 200, true));
        db.addDish(new Menu("dish-2", 100.0, 250, true));
        db.addDish(new Menu("dish-3", 90.0, 260, false));
        db.addDish(new Menu("dish-4", 50.0, 400, true));
        db.addDish(new Menu("dish-5", 100.0, 600, true));
        db.addDish(new Menu("dish-6", 90.0, 700, false));
        db.addDish(new Menu("dish-7", 90.0, 50, false));

        System.out.println("\n****************** getDishesByPrice(80, 110) ******************");
        List<Menu> dishes = db.getDishesByPrice(80, 110);
        for(Menu dish : dishes) {
            System.out.println(dish.toString());
        }
        System.out.println("\n****************** getDishesWithDiscount() ******************");
        dishes = db.getDishesWithDiscount();
        for(Menu dish : dishes) {
            System.out.println(dish.toString());
        }
        System.out.println("\n****************** findAllDishesSet(1000) ******************");
        db.findDishesSet(1000);

        MenuDbUtil.closeResourses();
    }
}

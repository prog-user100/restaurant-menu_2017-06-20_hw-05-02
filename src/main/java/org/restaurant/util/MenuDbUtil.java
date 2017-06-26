package org.restaurant.util;

import org.restaurant.entity.Menu;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.*;

/*
Создать таблицу «Меню в ресторане». Колонки: название блюда,
его стоимость, вес, наличие скидки. Написать код для
добавления записей в таблицу и их выборки по критериям
«стоимость от-до», «только со скидкой», выбрать набор блюд
так, чтобы их суммарный вес был не более 1 КГ.
 */

public class MenuDbUtil {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("MenuPU");
    private static EntityManager em = emf.createEntityManager();

    public void addDish(Menu menu) {
        em.getTransaction().begin();
        em.persist(menu);
        em.getTransaction().commit();
    }

    public List<Menu> getDishesByPrice(double min, double max) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Menu> criteria = builder.createQuery(Menu.class);
        //Metamodel m = em.getMetamodel();
        //EntityType<Menu> Menu_ = m.entity(Menu.class);
        Root<Menu> root = criteria.from(Menu.class);
        Path<Double> pricePath = root.get("price");
        criteria.select(root).where(builder.between(pricePath, min, max));
        List<Menu> dishes = em.createQuery(criteria).getResultList();
        return dishes;
    }

    public List<Menu> getDishesWithDiscount() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Menu> criteria = builder.createQuery(Menu.class);
        Root<Menu> root = criteria.from(Menu.class);
        criteria.select(root).where(builder.equal(root.get("discountAvailable"), true));
        List<Menu> dishes = em.createQuery(criteria).getResultList();
        return dishes;
    }

    public void findDishesSet(Integer maxTotalWeight) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Menu> criteria = builder.createQuery(Menu.class);
        Root<Menu> root = criteria.from(Menu.class);
        Path<Integer> weightPath = root.get("weight");
        criteria.select(root).where(builder.lessThanOrEqualTo(weightPath, maxTotalWeight));
        List<Menu> menu = em.createQuery(criteria).getResultList();
        Map<Long, Integer> idAndWeight = new HashMap<Long, Integer>();
        for(Menu dish : menu) {
            idAndWeight.put(dish.getId(), dish.getWeight());
        }
        List<Map<Long, Integer>> dishesSet = find(idAndWeight, maxTotalWeight);
        printDishesSet(dishesSet, menu, maxTotalWeight);
    }

    private void printDishesSet(List<Map<Long, Integer>> dishes, List<Menu> menu, Integer maxTotalWeight) {
        Map<Long, Menu> menuMap = new HashMap<Long, Menu>();
        for(int i = 0; i < menu.size(); i++) {
            menuMap.put(menu.get(i).getId(), menu.get(i));
        }

        System.out.println("*** Dishes set of the total weight no more than " + maxTotalWeight/1000 + " kg ***");
        for(int i = 0; i < dishes.size(); i++) {
            System.out.println("Dish Set #"+i);
            Map<Long, Integer> dish = dishes.get(i);
            int totalWeight = 0;
            for(Long id: dish.keySet()) {
                System.out.println(menuMap.get(id).toString());
                totalWeight  += dish.get(id);
            }
            System.out.println("total weight = " + totalWeight);
        }
    }

    private List<Map<Long, Integer>> find(Map<Long, Integer> idAndWeight, Integer maxWeight) {

        List<Map<Long, Integer>> weightsResult = new ArrayList<Map<Long, Integer>>();
        List<Map<Long, Integer>> weightsToAdd = new ArrayList<Map<Long, Integer>>();

        for(Long key : idAndWeight.keySet()) {
            final Long id = key;
            final Integer weight = idAndWeight.get(key);
            weightsToAdd.add(new HashMap<Long, Integer>() {{put(id, weight);}});
        }

        for(int i = 0; i < idAndWeight.size(); i++) {
            weightsToAdd = findVariants(weightsToAdd, idAndWeight, maxWeight);
            if(weightsToAdd.isEmpty()) {
                break;
            }
            weightsResult.addAll(weightsToAdd);
        }
        return weightsResult;
    }

    private List<Map<Long, Integer>> findVariants(List<Map<Long, Integer>> weightsList, Map<Long, Integer> idAndWeight, Integer maxWeight) {
        List<Map<Long, Integer>> selectedWeightsList = new ArrayList<Map<Long, Integer>>();
        for(int i = 0; i < weightsList.size(); i++) {
            Integer totalWeight = 0;
            Map<Long, Integer> weights = weightsList.get(i);
            for(Long id: weights.keySet()) {
                totalWeight += weights.get(id);
            }
            for(Long id : idAndWeight.keySet()) {
                Integer temp = totalWeight + idAndWeight.get(id);

                if (temp > maxWeight || weights.containsKey(id)) {
                    continue;
                }

                Map<Long, Integer> map = new HashMap<Long, Integer>();
                map.putAll(weights);
                map.put(id, idAndWeight.get(id));
                boolean isExists = false;
                for(Map<Long,Integer> selectedWeights : selectedWeightsList) {
                    if( selectedWeights.keySet().containsAll(map.keySet())) {
                        isExists = true;
                    }
                }
                if(isExists == false) {
                    selectedWeightsList.add(map);
                }
            }
        }
        return selectedWeightsList;
    }

    public static void closeResourses() {
        em.close();
        emf.close();
    }
}

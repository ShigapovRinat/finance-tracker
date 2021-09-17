package sirius.tinkoff.financial.tracker.util;

import sirius.tinkoff.financial.tracker.dao.entity.CategoryEntity;
import sirius.tinkoff.financial.tracker.dao.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

public class CreatorDefaultCategoryUtil {

    public static List<CategoryEntity> createDefaultCategories(UserEntity userEntity) {
        List<CategoryEntity> categoryEntityList = new ArrayList<>();
        CategoryEntity defaultCategory1 = new CategoryEntity()
                .setUser(userEntity)
                .setName("Зарплата")
                .setPictureId(2131165362L)
                .setIncome(true);
        categoryEntityList.add(defaultCategory1);
        CategoryEntity defaultCategory2 = new CategoryEntity()
                .setUser(userEntity)
                .setName("Подработка")
                .setPictureId(2131165362L)
                .setIncome(true);
        categoryEntityList.add(defaultCategory2);
        CategoryEntity defaultCategory3 = new CategoryEntity()
                .setUser(userEntity)
                .setName("Подарок")
                .setPictureId(2131165335L)
                .setIncome(true);
        categoryEntityList.add(defaultCategory3);
        CategoryEntity defaultCategory4 = new CategoryEntity()
                .setUser(userEntity)
                .setName("Капитализация")
                .setPictureId(2131165318L)
                .setIncome(true);
        categoryEntityList.add(defaultCategory4);
        CategoryEntity defaultCategory5 = new CategoryEntity()
                .setUser(userEntity)
                .setName("Кафе и рестораны")
                .setPictureId(2131165341L)
                .setIncome(false);
        categoryEntityList.add(defaultCategory5);
        CategoryEntity defaultCategory6 = new CategoryEntity()
                .setUser(userEntity)
                .setName("Супермаркеты")
                .setPictureId(2131165344L)
                .setIncome(false);
        categoryEntityList.add(defaultCategory6);
        CategoryEntity defaultCategory7 = new CategoryEntity()
                .setUser(userEntity)
                .setName("Спортзал")
                .setPictureId(2131165368L)
                .setIncome(false);
        categoryEntityList.add(defaultCategory7);
        CategoryEntity defaultCategory8 = new CategoryEntity()
                .setUser(userEntity)
                .setName("Транспорт")
                .setPictureId(2131165372L)
                .setIncome(false);
        categoryEntityList.add(defaultCategory8);
        CategoryEntity defaultCategory9 = new CategoryEntity()
                .setUser(userEntity)
                .setName("Бензин")
                .setPictureId(2131165334L)
                .setIncome(false);
        categoryEntityList.add(defaultCategory9);
        CategoryEntity defaultCategory10 = new CategoryEntity()
                .setUser(userEntity)
                .setName("Медицина")
                .setPictureId(2131165358L)
                .setIncome(false);
        categoryEntityList.add(defaultCategory10);
        CategoryEntity defaultCategory11 = new CategoryEntity()
                .setUser(userEntity)
                .setName("Квартплата")
                .setPictureId(2131165337L)
                .setIncome(false);
        categoryEntityList.add(defaultCategory11);
        CategoryEntity defaultCategory12 = new CategoryEntity()
                .setUser(userEntity)
                .setName("Отпуск")
                .setPictureId(2131165374L)
                .setIncome(false);
        categoryEntityList.add(defaultCategory12);
        return categoryEntityList;
    }
}

/**
 * Prepared Category entities used for test purposes
 */
package cz.muni.fi.dictatetrainer.commontests.category;

import cz.muni.fi.dictatetrainer.category.model.Category;
import org.junit.Ignore;

import java.util.Arrays;
import java.util.List;

@Ignore
public class CategoriesForTestRepository {

    public static Category vyjmenovanaSlovaCat() {
        return new Category("Vyjmenovana slova");
    }

    public static Category velkaPismena() {
        return new Category("Velka pismena");
    }

    public static Category interpunkce() {
        return new Category("Interpunkce");
    }

    public static Category categoryWithId(final Category category, final Long id) {
        category.setId(id);
        return category;
    }

    public static List<Category> allCategories() {
        return Arrays.asList(vyjmenovanaSlovaCat(), velkaPismena(), interpunkce());
    }
}

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

    public static Category vybraneSlova() {
        return new Category("Vybrane slova");
    }

    public static Category velkeMalePismena() {
        return new Category("Velke a male pismena");
    }

    public static Category interpunkcia() {
        return new Category("Interpunkcia");
    }

    public static Category categoryWithId(final Category category, final Long id) {
        category.setId(id);
        return category;
    }

    public static List<Category> allCategories() {
        return Arrays.asList(vybraneSlova(), velkeMalePismena(), interpunkcia());
    }
}

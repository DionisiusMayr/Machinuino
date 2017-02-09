package Machinuino;

import java.util.Collection;

public class Utils {

    private Utils() {
    }

    /**
     * Verify the Nullity of the {@link Collection} passed and throws a {@link NullPointerException}
     * if the collection itself is null or if one if it's element is null
     *
     * @param tag tag of the exception message
     * @param name name of the collection
     * @param c collection to be checked
     * @throws NullPointerException if the collection or an element of the collection is null
     */
    public static <T> void verifyCollectionNullity(String tag, String name, Collection<T> c) {
        if (c == null) throw new NullPointerException(tag + ": " + name + " was null!");
        for (T e : c) {
            if (e == null) {
                throw new NullPointerException(tag + ": an element of the " + name + " was null!");
            }
        }
    }

    /**
     * Verify the Nullity of the {@link Collection} passed and throws a {@link NullPointerException}
     * if the collection itself is null or if one if it's element is null
     *
     * @param c collection to be checked
     * @throws NullPointerException if the collection or an element of the collection passed is null
     */
    public static void verifyCollectionNullity(Collection<?> c) {
        verifyCollectionNullity("", "collection", c);
    }

    /**
     * Verify the Nullity of the generic passed and throws a {@link NullPointerException} if it is
     * null
     *
     * @param tag tag of the exception message
     * @param name name of the generic
     * @param t the generic to be checked
     * @throws NullPointerException if the generic passed is null
     */
    public static <T> void verifyNullity(String tag, String name, T t) {
        if (t == null) throw new NullPointerException(tag + ": " + name + " was null!");
    }

    /**
     * Verify the Nullity of the generic passed and throws a {@link NullPointerException} if it is
     * null
     *
     * @param t the generic to be checked
     * @throws NullPointerException if the generic passed is null
     */
    public static <T> void verifyNullity(T t) {
        verifyNullity("", "object", t);
    }

    /**
     * Verify the integrity of a collection, mainly verifying if a element is or is not on the
     * collection, if it should be and it isn't an {@link IllegalArgumentException} is thrown, the
     * opposite also throws the exception
     *
     * @param tag Tag of the exception message
     * @param comparedName name of the element to be verified
     * @param compared the element to be verified
     * @param collectionName name of the collection being verified
     * @param collection the collection being verified
     * @param shouldContain if the colletion should be contaning the element or not
     * @throws IllegalArgumentException if the element should be in the collection and it isn't or
     * the opposite
     */
    public static <T> void verifyCollectionIntegrity(String tag,
                                                     String comparedName,
                                                     T compared, String collectionName,
                                                     Collection<? extends T> collection,
                                                     boolean shouldContain) {
        if (collection.contains(compared) != shouldContain) {
            throw new IllegalArgumentException(tag + ": " + comparedName + " " + compared + " was "
                    + (shouldContain ? "not " : "") + "on " + collectionName + " " + collection);
        }
    }

    public static void verifyPositive(String tag, String name, int number) {
        if (number < 0) {
            throw new IllegalArgumentException(tag + ": Negative " + name + "!");
        }
    }
}

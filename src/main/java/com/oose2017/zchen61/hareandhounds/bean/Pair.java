package com.oose2017.zchen61.hareandhounds.bean;

/**
 * A tuple structure used to record and hash coordinates on the map
 *
 * This class is adopted from the stack overflow website.
 * Original page URL:
 * https://stackoverflow.com/questions/521171/a-java-collection-of-value-pairs-tuples
 */
public class Pair {

    private final int left;
    private final int right;

    public Pair(int left, int right) {
        this.left = left;
        this.right = right;
    }

    public int getLeft() { return left; }
    public int getRight() { return right; }

    @Override
    public int hashCode() { return ((left + right)*(left + right + 1)/2) + right; }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair)) return false;
        Pair pairo = (Pair) o;
        return this.left==(pairo.getLeft()) &&
                this.right==(pairo.getRight());
    }

}
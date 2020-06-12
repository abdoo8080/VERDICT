package com.ge.verdict.synthesis;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import com.ge.verdict.synthesis.dtree.DAnd;
import com.ge.verdict.synthesis.dtree.DLeaf;
import com.ge.verdict.synthesis.dtree.DOr;
import com.ge.verdict.synthesis.dtree.DTree;

public class App {
    /*
     * Note: currently must set LD_LIBRARY_PATH. For testing purposes
     * this can be done in Eclipse run configurations, but we will have
     * to figure this out in a cross-platform way and also set it up
     * in the Docker image.
     */

    public static void main(String[] args) {
		Optional<Set<DLeaf>> selected = VerdictSynthesis.performSynthesis(buildDemo());
		if (selected.isPresent()) {
			for (DLeaf leaf : selected.get()) {
				System.out.println("Selected leaf: " + leaf.prettyPrint());
			}
		}
    }

    private static DTree buildDemo() {
        return new DOr(
                Arrays.asList(
                        new DAnd(
                                Arrays.asList(
										DLeaf.createIfNeeded("A-comp", "A-def", "A-att", 1),
										DLeaf.createIfNeeded("B-comp", "B-def", "B-att",
												1))),
                        new DAnd(
                                Arrays.asList(
										DLeaf.createIfNeeded("B-comp", "B-def", "B-att", 1),
										DLeaf.createIfNeeded("C-comp", "C-def", "C-att",
												1))),
                        new DAnd(
                                Arrays.asList(
										DLeaf.createIfNeeded("A-comp", "A-def", "A-att", 1),
										DLeaf.createIfNeeded("C-comp", "C-def", "C-att", 1)))));
    }
}
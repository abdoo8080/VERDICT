package com.ge.verdict.synthesis;

import com.ge.verdict.attackdefensecollector.Prob;
import com.ge.verdict.attackdefensecollector.adtree.ADAnd;
import com.ge.verdict.attackdefensecollector.adtree.ADNot;
import com.ge.verdict.attackdefensecollector.adtree.ADOr;
import com.ge.verdict.attackdefensecollector.adtree.ADTree;
import com.ge.verdict.attackdefensecollector.adtree.Attack;
import com.ge.verdict.attackdefensecollector.adtree.Defense;
import com.ge.verdict.attackdefensecollector.model.CIA;
import com.ge.verdict.attackdefensecollector.model.SystemModel;
import com.ge.verdict.synthesis.dtree.DAnd;
import com.ge.verdict.synthesis.dtree.DLeaf;
import com.ge.verdict.synthesis.dtree.DNot;
import com.ge.verdict.synthesis.dtree.DOr;
import com.ge.verdict.synthesis.dtree.DTree;
import java.io.File;
import java.util.Collections;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class DTreeConstructorTest {
    @Test
    public void testFlattenNot() {
        // Note: comparing the results of prettyPrint() instead of the trees directly
        // because we have not implemented equals for the DTree classes

        DLeaf.Factory factory = new DLeaf.Factory();

        DTree leaf = factory.createIfNeeded("A", "A", "A", 0);
        DTree dtree = new DNot(new DNot(leaf));
        Assertions.assertThat(dtree.flattenNot().prettyPrint()).isEqualTo(leaf.prettyPrint());

        DTree dtree2 =
                new DAnd(Collections.singletonList(new DOr(Collections.singletonList(dtree))));
        DTree dtree3 =
                new DAnd(Collections.singletonList(new DOr(Collections.singletonList(leaf))));
        Assertions.assertThat(dtree2.flattenNot().prettyPrint()).isEqualTo(dtree3.prettyPrint());
    }

    @Test
    public void testConstruct() {
        DLeaf.Factory factory = new DLeaf.Factory();

        CostModel dummyCosts =
                new CostModel(new File(getClass().getResource("dummyCosts.xml").getPath()));
        int dal = 5;

        SystemModel system = new SystemModel("S1");

        Attack attack1 =
                new Attack(system.getAttackable(), "A1", "An attack", Prob.certain(), CIA.I);
        Defense defense1 = new Defense(attack1);
        defense1.addDefenseClause(
                Collections.singletonList(new Defense.DefenseLeaf("D1", Optional.empty())));

        ADTree adtree = new ADOr(new ADAnd(new ADNot(defense1), attack1));

        DTree dtree =
                new DAnd(
                        Collections.singletonList(
                                new DOr(
                                        Collections.singletonList(
                                                new DOr(
                                                        Collections.singletonList(
                                                                new DAnd(
                                                                        Collections.singletonList(
                                                                                factory
                                                                                        .createIfNeeded(
                                                                                                "S1",
                                                                                                "D1",
                                                                                                "A1",
                                                                                                dal)))))))));

        Assertions.assertThat(
                        DTreeConstructor.construct(adtree, dummyCosts, dal, factory).prettyPrint())
                .isEqualTo(dtree.prettyPrint());
    }
}

package com.intellij.history.core.changes;

import org.junit.Test;

public class ChangeListChainsTest extends ChangeListTestCase {
  @Test
  public void testBasicChain() {
    Change c1 = new CreateFileChange(1, "f", null, -1, false);
    Change c2 = new ContentChange("f", null, -1);

    applyAndAdd(c1, c2);

    assertEquals(array(c1, c2), cl.getChain(c1));
  }

  @Test
  public void testChangeNotInChain() {
    Change c1 = new CreateFileChange(1, "f1", null, -1, false);
    Change c2 = new CreateFileChange(2, "f2", null, -1, false);

    applyAndAdd(c1, c2);

    assertEquals(array(c1), cl.getChain(c1));
    assertEquals(array(c2), cl.getChain(c2));
  }

  @Test
  public void testChainedChangeSets() {
    Change c1 = cs(new CreateFileChange(1, "f1", null, -1, false), new CreateFileChange(2, "f2", null, -1, false));
    Change c2 = cs(new ContentChange("f1", null, -1));
    Change c3 = cs(new ContentChange("f2", null, -1));

    applyAndAdd(c1, c2, c3);

    assertEquals(array(c1, c2, c3), cl.getChain(c1));
    assertEquals(array(c2), cl.getChain(c2));
    assertEquals(array(c3), cl.getChain(c3));
  }

  @Test
  public void testMovementChain() {
    Change c1 = new CreateDirectoryChange(1, "dir1");
    Change c2 = new CreateDirectoryChange(2, "dir2");
    Change c3 = new CreateFileChange(3, "dir1/f", null, -1, false);

    applyAndAdd(c1, c2, c3);

    assertEquals(array(c1, c3), cl.getChain(c1));
    assertEquals(array(c2), cl.getChain(c2));

    Change c4 = new MoveChange("dir1/f", "dir2");
    applyAndAdd(c4);

    assertEquals(array(c1, c3, c4), cl.getChain(c1));
    assertEquals(array(c2, c4), cl.getChain(c2));
  }
}

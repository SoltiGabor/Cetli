package cetli;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  SurfaceTest.class,
  FamilyViewTest.class,
  FilterPaneTest.class,
})

public class AllTests {
  // the class remains empty,
  // used only as a holder for the above annotations
}

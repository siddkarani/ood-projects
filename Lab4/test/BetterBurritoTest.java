import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import betterburrito.ObservableBurrito;
import burrito.Burrito;
import burrito.CustomBurrito;
import burrito.PortionSize;
import burrito.Protein;
import burrito.Size;
import burrito.Topping;
import burrito.VeggieBurrito;

/**
 * This is the test class for the better burrito implementation.
 */
public class BetterBurritoTest {

  private Burrito originalCustomBurrito;
  private Burrito originalVeggieBurrito;
  private Burrito originalJumboVeggieBurrito;
  private Burrito originalVeggieBurritoLessCheese;

  @Before
  public void setup() {
    // Setup original burritos from the original test class
    originalCustomBurrito = new CustomBurrito(Size.Normal);
    originalCustomBurrito.addProtein(Protein.Tofu, PortionSize.Normal);
    originalCustomBurrito.addTopping(Topping.Cheese, PortionSize.Normal);
    originalCustomBurrito.addTopping(Topping.MediumSalsa, PortionSize.Less);
    originalCustomBurrito.addTopping(Topping.SourCream, PortionSize.Extra);

    originalVeggieBurrito = new VeggieBurrito(Size.Normal);
    originalJumboVeggieBurrito = new VeggieBurrito(Size.Jumbo);

    originalVeggieBurritoLessCheese = new VeggieBurrito(Size.Normal);
    originalVeggieBurritoLessCheese.addTopping(Topping.Cheese, PortionSize.Less);
  }

  @Test
  public void testCustomBurrito() {
    // Test CustomBurrito with builder
    ObservableBurrito customBurrito = new betterburrito.CustomBurrito.CustomBurritoBuilder()
            .size(Size.Normal)
            .addProtein(Protein.Tofu, PortionSize.Normal)
            .addTopping(Topping.Cheese, PortionSize.Normal)
            .addTopping(Topping.MediumSalsa, PortionSize.Less)
            .addTopping(Topping.SourCream, PortionSize.Extra)
            .build();

    // Verify cost matches original implementation
    assertEquals("Custom burrito cost should match original",
            originalCustomBurrito.cost(), customBurrito.cost(), 0.01);

    // Verify toppings and proteins
    assertEquals("Should have normal tofu",
            PortionSize.Normal, customBurrito.hasProtein(Protein.Tofu));
    assertEquals("Should have normal cheese",
            PortionSize.Normal, customBurrito.hasTopping(Topping.Cheese));
    assertEquals("Should have less medium salsa",
            PortionSize.Less, customBurrito.hasTopping(Topping.MediumSalsa));
    assertEquals("Should have extra sour cream",
            PortionSize.Extra, customBurrito.hasTopping(Topping.SourCream));
    assertNull("Should not have black beans",
            customBurrito.hasProtein(Protein.BlackBeans));
  }

  @Test
  public void testVeggieBurrito() {
    // Test VeggieBurrito with builder (normal size)
    ObservableBurrito veggieBurritoNormalSize = new betterburrito.VeggieBurrito
            .VeggieBurritoBuilder()
            .build();

    // Verify cost matches original implementation
    assertEquals("Normal veggie burrito cost should match original",
            originalVeggieBurrito.cost(), veggieBurritoNormalSize.cost(), 0.01);

    // Test jumbo size
    ObservableBurrito veggieBurritoJumboSize = new betterburrito.VeggieBurrito
            .VeggieBurritoBuilder()
            .size(Size.Jumbo)
            .build();

    // Verify cost matches original implementation
    assertEquals("Jumbo veggie burrito cost should match original",
            originalJumboVeggieBurrito.cost(), veggieBurritoJumboSize.cost(), 0.01);

    // Verify ingredients
    assertEquals("Should have normal black beans",
            PortionSize.Normal, veggieBurritoJumboSize.hasProtein(Protein.BlackBeans));
    assertEquals("Should have normal medium salsa",
            PortionSize.Normal, veggieBurritoJumboSize.hasTopping(Topping.MediumSalsa));
    assertEquals("Should have normal cheese",
            PortionSize.Normal, veggieBurritoJumboSize.hasTopping(Topping.Cheese));
    assertEquals("Should have normal lettuce",
            PortionSize.Normal, veggieBurritoJumboSize.hasTopping(Topping.Lettuce));
    assertEquals("Should have normal guacamole",
            PortionSize.Normal, veggieBurritoJumboSize.hasTopping(Topping.Guacamole));
  }

  @Test
  public void testVeggieBurritoLessCheese() {
    // Test veggie burrito with less cheese
    ObservableBurrito veggieBurritoLessCheese = new betterburrito.VeggieBurrito
            .VeggieBurritoBuilder()
            .addTopping(Topping.Cheese, PortionSize.Less)
            .build();

    // Compare with original implementation
    assertEquals("Veggie burrito with less cheese cost should match original",
            originalVeggieBurritoLessCheese.cost(), veggieBurritoLessCheese.cost(),
            0.01);

    // Verify less cheese
    assertEquals("Should have less cheese",
            PortionSize.Less, veggieBurritoLessCheese.hasTopping(Topping.Cheese));
  }

  @Test
  public void testNoGuacamole() {
    // Test veggie burrito without guacamole
    ObservableBurrito veggieBurritoNoGuac = new betterburrito.VeggieBurrito
            .VeggieBurritoBuilder()
            .noGuacamole()
            .build();

    // Verify no guacamole
    assertNull("Should not have guacamole",
            veggieBurritoNoGuac.hasTopping(Topping.Guacamole));

    // Verify other ingredients are still present
    assertEquals("Should have normal black beans",
            PortionSize.Normal, veggieBurritoNoGuac.hasProtein(Protein.BlackBeans));
    assertEquals("Should have normal medium salsa",
            PortionSize.Normal, veggieBurritoNoGuac.hasTopping(Topping.MediumSalsa));
    assertEquals("Should have normal cheese",
            PortionSize.Normal, veggieBurritoNoGuac.hasTopping(Topping.Cheese));
    assertEquals("Should have normal lettuce",
            PortionSize.Normal, veggieBurritoNoGuac.hasTopping(Topping.Lettuce));
  }

  @Test
  public void testNoBlackBeans() {
    // Test veggie burrito without black beans
    ObservableBurrito veggieBurritoNoBeans = new betterburrito.VeggieBurrito
            .VeggieBurritoBuilder()
            .noBlackBeans()
            .build();

    // Verify no black beans
    assertNull("Should not have black beans",
            veggieBurritoNoBeans.hasProtein(Protein.BlackBeans));
  }

  @Test
  public void testNoMediumSalsa() {
    // Test veggie burrito without medium salsa
    ObservableBurrito veggieBurritoNoSalsa = new betterburrito.VeggieBurrito
            .VeggieBurritoBuilder()
            .noMediumSalsa()
            .build();

    // Verify no medium salsa
    assertNull("Should not have medium salsa",
            veggieBurritoNoSalsa.hasTopping(Topping.MediumSalsa));
  }

  @Test
  public void testNoLettuce() {
    // Test veggie burrito without lettuce
    ObservableBurrito veggieBurritoNoLettuce = new betterburrito.VeggieBurrito
            .VeggieBurritoBuilder()
            .noLettuce()
            .build();

    // Verify no lettuce
    assertNull("Should not have lettuce",
            veggieBurritoNoLettuce.hasTopping(Topping.Lettuce));
  }

  @Test
  public void testNoCheese() {
    // Test veggie burrito without cheese
    ObservableBurrito veggieBurritoNoCheese = new betterburrito.VeggieBurrito
            .VeggieBurritoBuilder()
            .noCheese()
            .build();

    // Verify no cheese
    assertNull("Should not have cheese",
            veggieBurritoNoCheese.hasTopping(Topping.Cheese));
  }

  @Test
  public void testMultipleCustomizations() {
    // Test multiple customizations
    ObservableBurrito customizedVeggieBurrito = new betterburrito.VeggieBurrito
            .VeggieBurritoBuilder()
            .noBlackBeans()
            .noMediumSalsa()
            .noLettuce()
            .noCheese()
            .addTopping(Topping.HotSalsa, PortionSize.Extra)
            .addProtein(Protein.Tofu, PortionSize.Normal)
            .build();

    // Verify customizations
    assertNull("Should not have black beans",
            customizedVeggieBurrito.hasProtein(Protein.BlackBeans));
    assertNull("Should not have medium salsa",
            customizedVeggieBurrito.hasTopping(Topping.MediumSalsa));
    assertNull("Should not have lettuce",
            customizedVeggieBurrito.hasTopping(Topping.Lettuce));
    assertNull("Should not have cheese",
            customizedVeggieBurrito.hasTopping(Topping.Cheese));
    assertEquals("Should have extra hot salsa",
            PortionSize.Extra, customizedVeggieBurrito.hasTopping(Topping.HotSalsa));
    assertEquals("Should have normal tofu",
            PortionSize.Normal, customizedVeggieBurrito.hasProtein(Protein.Tofu));
  }

  @Test
  public void testChainedCustomizationMethods() {
    // Test that customization methods correctly return the proper builder type
    ObservableBurrito veggieBurrito = new betterburrito.VeggieBurrito.VeggieBurritoBuilder()
            .size(Size.Jumbo)  // Returns VeggieBurritoBuilder
            .noGuacamole()     // Should work because size() returns VeggieBurritoBuilder
            .build();

    assertNull("Should not have guacamole",
            veggieBurrito.hasTopping(Topping.Guacamole));

    // Calculate expected cost - Jumbo base cost plus all default ingredients except guacamole
    double ingredientsCost = 0;
    ingredientsCost += Protein.BlackBeans.getCost() * PortionSize.Normal.getCostMultipler();
    ingredientsCost += Topping.MediumSalsa.getCost() * PortionSize.Normal.getCostMultipler();
    ingredientsCost += Topping.Cheese.getCost() * PortionSize.Normal.getCostMultipler();
    ingredientsCost += Topping.Lettuce.getCost() * PortionSize.Normal.getCostMultipler();
    // No guacamole because we removed it

    assertEquals("Should be jumbo size",
            Size.Jumbo.getBaseCost() + ingredientsCost, veggieBurrito.cost(),
            0.01);
  }

  @Test
  public void testEmptyCustomBurrito() {
    // Test empty burrito
    ObservableBurrito emptyBurrito = new betterburrito.CustomBurrito.CustomBurritoBuilder()
            .size(Size.Normal)
            .build();

    assertEquals("Empty burrito should only cost the base amount",
            Size.Normal.getBaseCost(), emptyBurrito.cost(), 0.01);
  }

  @Test
  public void testOverrideProteinsAndToppings() {
    // Test overriding proteins and toppings
    ObservableBurrito overriddenBurrito = new betterburrito.CustomBurrito.CustomBurritoBuilder()
            .size(Size.Normal)
            .addProtein(Protein.Chicken, PortionSize.Normal)
            .addProtein(Protein.Chicken, PortionSize.Extra) // Override
            .addTopping(Topping.Cheese, PortionSize.Less)
            .addTopping(Topping.Cheese, PortionSize.Normal) // Override
            .build();

    assertEquals("Should have extra chicken",
            PortionSize.Extra, overriddenBurrito.hasProtein(Protein.Chicken));
    assertEquals("Should have normal cheese",
            PortionSize.Normal, overriddenBurrito.hasTopping(Topping.Cheese));
  }

  @Test
  public void testAllProteinsAndToppings() {
    // Test builder with all toppings and proteins
    betterburrito.CustomBurrito.CustomBurritoBuilder allBuilder =
            new betterburrito.CustomBurrito.CustomBurritoBuilder()
                    .size(Size.Jumbo);

    // Add all proteins
    for (Protein protein : Protein.values()) {
      allBuilder.addProtein(protein, PortionSize.Normal);
    }

    // Add all toppings
    for (Topping topping : Topping.values()) {
      allBuilder.addTopping(topping, PortionSize.Normal);
    }

    ObservableBurrito loadedBurrito = allBuilder.build();

    // Verify all proteins and toppings are present
    for (Protein protein : Protein.values()) {
      assertNotNull("Should have " + protein,
              loadedBurrito.hasProtein(protein));
    }

    for (Topping topping : Topping.values()) {
      assertNotNull("Should have " + topping,
              loadedBurrito.hasTopping(topping));
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testMissingSizeThrowsException() {
    // This should throw IllegalStateException since we're not setting a size
    new betterburrito.CustomBurrito.CustomBurritoBuilder()
            .addProtein(Protein.Chicken, PortionSize.Normal)
            .build();
  }

  @Test
  public void testComplexBurrito() {
    // Test a complex burrito with multiple proteins and toppings
    ObservableBurrito complexBurrito = new betterburrito.CustomBurrito.CustomBurritoBuilder()
            .size(Size.Jumbo)
            .addProtein(Protein.Chicken, PortionSize.Extra)
            .addProtein(Protein.Fish, PortionSize.Less)
            .addTopping(Topping.Guacamole, PortionSize.Extra)
            .addTopping(Topping.HotSalsa, PortionSize.Extra)
            .addTopping(Topping.Cheese, PortionSize.Normal)
            .addTopping(Topping.SourCream, PortionSize.Less)
            .build();

    // Calculate expected cost manually
    double expectedCost = Size.Jumbo.getBaseCost();
    expectedCost += Protein.Chicken.getCost() * PortionSize.Extra.getCostMultipler();
    expectedCost += Protein.Fish.getCost() * PortionSize.Less.getCostMultipler();
    expectedCost += Topping.Guacamole.getCost() * PortionSize.Extra.getCostMultipler();
    expectedCost += Topping.HotSalsa.getCost() * PortionSize.Extra.getCostMultipler();
    expectedCost += Topping.Cheese.getCost() * PortionSize.Normal.getCostMultipler();
    expectedCost += Topping.SourCream.getCost() * PortionSize.Less.getCostMultipler();

    assertEquals("Complex burrito cost should be calculated correctly",
            expectedCost, complexBurrito.cost(), 0.01);
  }

  @Test
  public void testVeggieBurritoWithNoDefaults() {
    // Create a veggie burrito builder and remove all default ingredients
    ObservableBurrito emptyVeggieBurrito = new betterburrito.VeggieBurrito.VeggieBurritoBuilder()
            .noBlackBeans()
            .noMediumSalsa()
            .noCheese()
            .noLettuce()
            .noGuacamole()
            .build();

    // Should be same as an empty custom burrito
    ObservableBurrito emptyCustomBurrito = new betterburrito.CustomBurrito.CustomBurritoBuilder()
            .size(Size.Normal)
            .build();

    assertEquals("Empty veggie burrito should cost the same as empty custom burrito",
            emptyCustomBurrito.cost(), emptyVeggieBurrito.cost(), 0.01);
  }

  @Test
  public void testMultipleBuilderCalls() {
    // Test multiple separate builder calls
    betterburrito.CustomBurrito.CustomBurritoBuilder builder =
            new betterburrito.CustomBurrito.CustomBurritoBuilder()
                    .size(Size.Normal);

    builder.addProtein(Protein.Chicken, PortionSize.Normal);
    builder.addTopping(Topping.Cheese, PortionSize.Normal);

    ObservableBurrito burrito = builder.build();

    assertEquals("Should have normal chicken",
            PortionSize.Normal, burrito.hasProtein(Protein.Chicken));
    assertEquals("Should have normal cheese",
            PortionSize.Normal, burrito.hasTopping(Topping.Cheese));
  }

  @Test
  public void testProteinOnlyBurrito() {
    // Test burrito with only proteins, no toppings
    ObservableBurrito proteinBurrito = new betterburrito.CustomBurrito.CustomBurritoBuilder()
            .size(Size.Normal)
            .addProtein(Protein.Chicken, PortionSize.Normal)
            .addProtein(Protein.BlackBeans, PortionSize.Extra)
            .build();

    // Calculate expected cost
    double expectedCost = Size.Normal.getBaseCost();
    expectedCost += Protein.Chicken.getCost() * PortionSize.Normal.getCostMultipler();
    expectedCost += Protein.BlackBeans.getCost() * PortionSize.Extra.getCostMultipler();

    assertEquals("Protein burrito cost should be calculated correctly",
            expectedCost, proteinBurrito.cost(), 0.01);

    // Verify no toppings
    for (Topping topping : Topping.values()) {
      assertNull("Should not have topping " + topping,
              proteinBurrito.hasTopping(topping));
    }
  }

  @Test
  public void testToppingOnlyBurrito() {
    // Test burrito with only toppings, no proteins
    ObservableBurrito toppingBurrito = new betterburrito.CustomBurrito.CustomBurritoBuilder()
            .size(Size.Normal)
            .addTopping(Topping.Guacamole, PortionSize.Extra)
            .addTopping(Topping.HotSalsa, PortionSize.Normal)
            .build();

    // Calculate expected cost
    double expectedCost = Size.Normal.getBaseCost();
    expectedCost += Topping.Guacamole.getCost() * PortionSize.Extra.getCostMultipler();
    expectedCost += Topping.HotSalsa.getCost() * PortionSize.Normal.getCostMultipler();

    assertEquals("Topping burrito cost should be calculated correctly",
            expectedCost, toppingBurrito.cost(), 0.01);

    // Verify no proteins
    for (Protein protein : Protein.values()) {
      assertNull("Should not have protein " + protein,
              toppingBurrito.hasProtein(protein));
    }
  }

  @Test
  public void testVeggieBurritoWithCustomProtein() {
    // Test veggie burrito with a custom protein
    ObservableBurrito veggieBurritoWithChicken = new betterburrito.VeggieBurrito
            .VeggieBurritoBuilder()
            .noBlackBeans() // Remove default protein
            .addProtein(Protein.Chicken, PortionSize.Extra) // Add custom protein
            .build();

    // Verify custom protein
    assertNull("Should not have black beans",
            veggieBurritoWithChicken.hasProtein(Protein.BlackBeans));
    assertEquals("Should have extra chicken",
            PortionSize.Extra, veggieBurritoWithChicken.hasProtein(Protein.Chicken));
  }

  @Test
  public void testImmutabilityAfterBuild() {
    // Create a builder and build a burrito
    betterburrito.CustomBurrito.CustomBurritoBuilder builder =
            new betterburrito.CustomBurrito.CustomBurritoBuilder()
                    .size(Size.Normal)
                    .addProtein(Protein.Chicken, PortionSize.Normal);

    ObservableBurrito burrito1 = builder.build();

    // Modify the builder after building
    builder.addTopping(Topping.Cheese, PortionSize.Normal);

    ObservableBurrito burrito2 = builder.build();

    // Verify burrito1 is unaffected by changes to the builder
    assertNull("First burrito should not have cheese",
            burrito1.hasTopping(Topping.Cheese));

    // Verify burrito2 has the new topping
    assertEquals("Second burrito should have normal cheese",
            PortionSize.Normal, burrito2.hasTopping(Topping.Cheese));
  }
}
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.testng.annotations.Test;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.testng.Assert.*;

public class RozetkaTest {
    private final String menuCategories = "a.menu-categories__link";
    private final String menuSubcategories = "div.tile-cats";
    private final String productTile = "div.goods-tile";
    private final String productPrice = "span.goods-tile__price-value";
    private final String compareButton = "button.compare-button";
    private final String compareCounter = "span.counter";
    private final String productName = "h1.product__title";
    private final String productPriceBig = "p.product-prices__big";
    private final String compareHeaderButton = "button[aria-label='Списки сравнения']";
    private final String comparisonModalLink = "a.comparison-modal__link";
    private final String comparisonProducts = "li.products-grid__cell";
    private final String comparisonProductNames = "a.product__heading";
    private final String comparisonProductPrices = "div.product__price";

    @Test
    public void addMonitorsToComparison() {
        open("https://rozetka.com.ua/");
        $$(menuCategories).findBy(text("Ноутбуки и компьютеры")).click();
        $$(menuSubcategories).findBy(text("Мониторы")).click();

        clickOnProductWithPriceLessThan(8000);
        $(compareButton).click();
        $(compareCounter).shouldHave(text("1"));
        String firstMonitorName = getProductName();
        double firstMonitorPrice = getProductPrice();
        back();

        clickOnProductWithPriceLessThan(firstMonitorPrice);
        $(compareButton).click();
        $(compareCounter).shouldHave(text("2"));
        String secondMonitorName = getProductName();
        double secondMonitorPrice = getProductPrice();

        $(compareHeaderButton).click();
        $$(comparisonModalLink).findBy(text("Мониторы")).click();
        $$(comparisonProducts).shouldHave(size(2));
        verifyComparisonProductNameAndPrice(firstMonitorName, firstMonitorPrice);
        verifyComparisonProductNameAndPrice(secondMonitorName, secondMonitorPrice);
    }

    public void clickOnProductWithPriceLessThan(double price) {
        ElementsCollection products = $$(productTile).shouldHave(sizeGreaterThan(0));
        for (SelenideElement product : products) {
            String productPrice = product.find(this.productPrice).text().replaceAll("[^0-9]", "");
            if (Double.parseDouble(productPrice) < price) {
                product.click();
                $(productName).shouldBe(visible);
                break;
            }
        }
    }

    public String getProductName() {
        return $(productName).text().trim();
    }

    public double getProductPrice() {
        return Double.parseDouble($(productPriceBig).text().replaceAll("[^0-9]", ""));
    }

    public String getComparisonProductName(String productName) {
        SelenideElement comparisonProduct = $$(comparisonProducts).findBy(text(productName));
        return comparisonProduct.find(comparisonProductNames).text().trim();
    }

    public double getComparisonProductPrice(String productName) {
        SelenideElement comparisonProduct = $$(comparisonProducts).findBy(text(productName));
        return Double.parseDouble(comparisonProduct.find(comparisonProductPrices).getOwnText()
                .replaceAll("[^0-9]", ""));
    }

    public void verifyComparisonProductNameAndPrice(String productName, double productPrice) {
        assertTrue(getComparisonProductName(productName).contains(productName));
        assertEquals(getComparisonProductPrice(productName), productPrice);
    }

}

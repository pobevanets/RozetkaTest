import org.openqa.selenium.By;
import org.testng.annotations.Test;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class GoogleSearchTest {
    private final String results = "#res .g";

    @Test
    public void GoogleSearchTest() {
        open("https://google.com/");
        $(By.name("q")).val("iphone kyiv buy").pressEnter();
        $$(results).shouldHave(sizeGreaterThan(1));

        int pageNumberWithSearchedLink = 0;
        for (int i = 1; i<=5; i++) {
            if ($$(results).findBy(text("allo.ua")).is(visible)) {
                System.out.println("ALLO.UA found on " + i + " page");
                pageNumberWithSearchedLink = i;
                break;
            }
            $$("td").findBy(text(String.valueOf(i+1))).click();
        }
        if (pageNumberWithSearchedLink==0) {
            System.out.println("ALLO.UA not found on first 5 pages");
        }

    }
}

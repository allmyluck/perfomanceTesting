import org.junit.*;


public class payMeanTest {
    private payMean payMeans;

    @Before
    public void setUp() throws Exception {
        payMeans = new payMean();
        payMeans.addInList(15, "r", "RUR", "id1", "true", "true", "true", "true");
        payMeans.addInList(0.01, "c", "RUR", "id2", "true", "true", "true", "false");
        payMeans.addInList(10, "c", "RUR", "id2", "true", "true", "true", "false");
    }

    @After
    public void teardown() {
        payMeans = null;
    }


    @Test
    public void checkOwnTransfer() {
        String result = payMeans.checkOwnTransfer();
        System.out.println(payMeans.findTransferFrom());
        System.out.println(payMeans.findTransferTo());
        Assert.assertEquals("true", result);
    }

}

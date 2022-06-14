
class payMean {
    private ArrayList<mean> allMeans
    private int indexFrom
    private int indexTo
    private int amountAllTo
    private int amountWb

    payMean() {
        this.allMeans = new ArrayList<>()
        indexFrom = -1
        indexTo = -1
        amountWb = 0
        amountAllTo = 0
    }


    public static void main(String[] args) {
    }


    public void addInList(double balance, String type, String isoCode, String id, String convTransferTo, String convTransferFrom, String transferTo, String transferFrom) {
        if(type == "c") {
            allMeans.add(new card(balance, type, isoCode, id, convTransferTo, convTransferFrom, transferTo, transferFrom))
        }
        if(type == "r") {
            allMeans.add(new rest(balance, type, isoCode, id, convTransferTo, convTransferFrom, transferTo, transferFrom))
        }
        /*
        if(type == "d") {
            allMeans.add(new deposit(balance, type, isoCode, id, convTransferTo, convTransferFrom, transferTo, transferFrom))
        }
         */
        if(transferFrom == "true") {
            amountWb++
        }

        if(transferTo == "true") {// Получатель, также может отправить деньги
            if((transferFrom == "true") || (transferFrom == "false" && (balance <= 0.01 && balance >= 0))) {
                amountAllTo++
            }
        }
    }

    //checking
    public String checkOwnTransfer() {
        if(amountWb > 0 && amountAllTo > 1) {// min - 1 отправитель и 1 получатель
            this.findTransferFrom()
            this.findTransferTo()
            if(this.checkFindMax() == "true") {
                this.findTransferFromMax()
            }
            if(indexFrom != -1 && indexTo != -1 && indexFrom != indexTo) {
                return "true"
            } else {
                return "false"
            }
        }
        return "false"
    }

    public String findById(String Id) {
        for(int i = 0; i < allMeans.size(); i++) {
            if(allMeans.get(i).getId() == Id) {
                return (allMeans.get(i).getId() + ";" + (allMeans.get(i).getBalance()).toString())
            }
        }
        return ""
    }

    public String checkConv() { // iso отправителя и получателя не равны
        if(isoFrom() != isoTo() && checkConvFrom() == "true" && checkConvTo() == "true") {
            return "true"
        }
        return "false"
    }

    public String checkFindMax() { // как минимум 2 отправителя
        if(amountWb > 1) {
            return "true"
        }
        return "false"
    }


    public String checkChangeTo() { // как минимум 2 получателя
        if(amountAllTo > 1) {
            return "true"
        }
        return "false"
    }

    //getters from or to
    public String findTransferFrom() {
        for(int i = 0; i < allMeans.size(); i++) {
            if(allMeans.get(i).getTransferFrom() == "true") {
                if(allMeans.get(i).getBalance() >= 0.01 && indexFrom != i) {
                    if(i != indexTo) {// если отправитель != получатель
                        indexFrom = i
                        return allMeans.get(i).getId()
                    } else { // если отправитель == получатель
                        if(allMeans.get(indexFrom).getTransferTo() == "true") {
                            // если можно свапнуть местами отп. и пол.
                            indexTo = indexFrom
                            indexFrom = i
                            return allMeans.get(i).getId()
                        }
                    }
                }
            }
        }
        return ""
    }

    public String findTransferTo() {
        for(int i = 0; i < allMeans.size(); i++) {
            if(allMeans.get(i).getTransferTo() == "true"
                    && (allMeans.get(i).getTransferFrom() == "true" || (allMeans.get(i).getTransferFrom() == "false" && (allMeans.get(i).getBalance() <= 0.01 && allMeans.get(i).getBalance() >= 0)))
                    && indexFrom != i && indexTo != i) {
                indexTo = i
                return allMeans.get(i).getId()
            }
        }
        return ""
    }


    public String findTransferFromMax() {
        boolean findResult = false
        int indexTemp = indexFrom
        for(int i = 0; i < allMeans.size(); i++) {
            if(allMeans.get(i).getTransferFrom() == "true") {
                double curBalance = allMeans.get(i).balance
                if(allMeans.get(indexTemp).getBalance() < curBalance) {
                    indexTemp = i
                    findResult = true
                }
            }
        }
        if(findResult) { // если найден результат
            if(indexTo == indexTemp) { // новый отправитель = получатель
                if(allMeans.get(indexFrom).getTransferTo() == "true") { // если можно свапнуть
                    indexTo = indexFrom
                    indexFrom = indexTemp
                } else { // если нельзя, ищу нового получателя
                    indexFrom = indexTemp
                    this.findTransferTo()
                }
            } else {
                indexFrom = indexTemp
            }
            return "true"
        }
        return "false"
    }

    //getters amount
    public double getSum() {
        double pointsTo = allMeans.get(indexFrom).getBalance() * 0.03
        if(pointsTo < 0.1) {
            pointsTo = 0.1
        }
        pointsTo = pointsTo.round(2)
        return pointsTo
    }
    //getters

    public String getSumConv(double curRate, String baseCurrency) {
        double curToNew
        double curBalance = allMeans.get(indexFrom).getBalance()
        if(this.isoFrom() == baseCurrency) {
            // ...
        } else {
            // ...
        }
        return  (curBalance.toString() + ";" + curToNew.toString())
    }


    public String checkConvFrom() {
        return allMeans.get(indexFrom).getConvTransferFrom()
    }

    public String checkConvTo() {
        return allMeans.get(indexTo).getConvTransferTo()
    }

    public String transferFrom() {
        return allMeans.get(indexFrom).getId()
    }

    public String transferTo() {
        return allMeans.get(indexTo).getId()
    }

    public String isoFrom() {
        return allMeans.get(indexFrom).getIsoCode()
    }

    public String isoTo() {
        return allMeans.get(indexTo).getIsoCode()
    }

    //close
    abstract class mean {
        private double balance
        private String type
        private String isoCode
        private String id
        private String convTransferTo
        private String convTransferFrom
        private String TransferTo
        private String TransferFrom

        mean(double balance, String type, String isoCode, String id, String convTransferTo, String convTransferFrom, String transferTo, String transferFrom) {
            this.balance = balance
            this.type = type
            this.isoCode = isoCode
            this.id = id
            this.convTransferTo = convTransferTo
            this.convTransferFrom = convTransferFrom
            TransferTo = transferTo
            TransferFrom = transferFrom
        }

        double getBalance() {
            return balance
        }

        String getType() {
            return type
        }

        String getIsoCode() {
            return isoCode
        }

        String getId() {
            return id
        }

        String getConvTransferTo() {
            return convTransferTo
        }

        String getConvTransferFrom() {
            return convTransferFrom
        }

        String getTransferTo() {
            return TransferTo
        }

        String getTransferFrom() {
            return TransferFrom
        }
    }

    class card extends mean {
        card(double balance, String type, String isoCode, String id, String convTransferTo, String convTransferFrom, String transferTo, String transferFrom) {
            super(balance, type, isoCode, id, convTransferTo, convTransferFrom, transferTo, transferFrom)
        }
    }


    class deposit extends mean {
        deposit(double balance, String type, String isoCode, String id, String convTransferTo, String convTransferFrom, String transferTo, String transferFrom) {
            super(balance, type, isoCode, id, convTransferTo, convTransferFrom, transferTo, transferFrom)
        }
    }


    class rest extends mean{
        rest(double balance, String type, String isoCode, String id, String convTransferTo, String convTransferFrom, String transferTo, String transferFrom) {
            super(balance, type, isoCode, id, convTransferTo, convTransferFrom, transferTo, transferFrom)
        }
    }
}

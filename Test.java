class Test {
    int amount;
    String type;
    String param;
    String mode;
    String code1;
    String code2;
    String code3;

    // Constructor
    public Test(int amount, String type, String param, String mode, String code1, String code2, String code3) {
        this.amount = amount;
        this.type = type;
        this.param = param;
        this.mode = mode;
        this.code1 = code1;
        this.code2 = code2;
        this.code3 = code3;
    }

    // Getters
    public int getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getParam() {
        return param;
    }

    public String getMode() {
        return mode;
    }

    public String getCode1() {
        return code1;
    }

    public String getCode2() {
        return code2;
    }

    public String getCode3() {
        return code3;
    }

    @Override
    public String toString() {
        return "Test{" +
                "amount=" + amount +
                ", type='" + type + '\'' +
                ", param='" + param + '\'' +
                ", mode='" + mode + '\'' +
                ", code1='" + code1 + '\'' +
                ", code2='" + code2 + '\'' +
                ", code3='" + code3 + '\'' +
                '}';
    }
}
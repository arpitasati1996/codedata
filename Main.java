public class Main {
    public static void main(String[] args) {
        // Creating the list of Test objects
        List<Test> testList = new ArrayList<>();

        // Creating and adding objects to the list with code1, code2, and code3
        Test test1 = new Test(20, "EG", "EE", "1", "C1", "C2", "C3");
        Test test2 = new Test(30, "EG", "EE", "1", "C1", "C2", "C3");
        Test test3 = new Test(40, "EG", "EE", "1", "C1", "C1", "C3");
        Test test4 = new Test(35, "EG", "EE", "2", "C2", "C3", "C5");
        Test test5 = new Test(40, "EG", "EE", "2", "C2", "C3", "C5");

        testList.add(test1);
        testList.add(test2);
        testList.add(test3);
        testList.add(test4);
        testList.add(test5);

        // Grouping by type, param, mode, code1, code2, code3, and calculating the average amount
        Map<String, Double> groupedData = testList.stream()
            .collect(Collectors.groupingBy(
                test -> test.getType() + "," + test.getParam() + "," + test.getMode() + "," +
                        test.getCode1() + "," + test.getCode2() + "," + test.getCode3(),
                Collectors.averagingInt(Test::getAmount)
            ));

        // Printing the grouped and averaged data in the desired output format
        groupedData.forEach((group, averageAmount) -> {
            System.out.println("\"" + group + "\" = " + averageAmount);
        });
    }
}
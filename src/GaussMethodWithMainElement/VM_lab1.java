package GaussMethodWithMainElement;

public class VM_lab1 {

    public static void main(String[] args) {
        GaussMethodWithMainElement elem = new GaussMethodWithMainElement();
        int correct = elem.readInput();
        if (correct == 0) {
            elem.init();
            elem.findResult();
        }
    }
}
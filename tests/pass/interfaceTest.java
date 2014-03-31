package pass;

interface interfacetester {
    void printName();
}

public class interfaceTest implements interfacetester {
    String name ;

    public interfaceTest(String name){
        this.name =name;
    }

    void printName(){
        System.out.println(this.name);
    }


}
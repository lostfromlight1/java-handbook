public class StackHeapReferencesDemo {

    public static void main(String[] args) {
        int primitiveOnStack = 7;
        String referenceOnStack = new String("heap-object");
        System.out.println("primitive=" + primitiveOnStack);
        System.out.println("identityHash=" + System.identityHashCode(referenceOnStack));
        System.out.println("sameRef=" + (referenceOnStack == new String("heap-object")));
        System.out.println("equalContent=" + referenceOnStack.equals("heap-object"));
    }
}

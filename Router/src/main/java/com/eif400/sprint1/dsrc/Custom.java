package dsrc; import java.util.*; import java.util.stream.*; import java.util.function.*; public class Custom { Custom(){} static int sum(List<Integer> list) {
    return list.stream().reduce(0, (a, n) -> a + n);
} }
package br.com.motiva.util;

public final class Assert {

    private Assert() {
    }

    public static void assertTrue(boolean condicao, String mensagem) {
        if (!condicao) {
            throw new AssertionError(mensagem);
        }
    }

    public static void assertFalse(boolean condicao, String mensagem) {
        assertTrue(!condicao, mensagem);
    }

    public static void assertNotNull(Object valor, String mensagem) {
        assertTrue(valor != null, mensagem);
    }

    public static void assertEquals(Object esperado, Object obtido, String mensagem) {
        assertTrue(esperado.equals(obtido), mensagem + " (esperado: " + esperado + ", obtido: " + obtido + ")");
    }

    public static void assertEquals(double esperado, double obtido, String mensagem) {
        assertTrue(Math.abs(esperado - obtido) < 0.001, mensagem + " (esperado: " + esperado + ", obtido: " + obtido + ")");
    }

    public static void assertEquals(int esperado, int obtido, String mensagem) {
        assertTrue(esperado == obtido, mensagem + " (esperado: " + esperado + ", obtido: " + obtido + ")");
    }
}

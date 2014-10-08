package models.daos;

public class DAOException extends RuntimeException {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static class FalloLoginException extends DAOException {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public FalloLoginException() {
            super("Usuario o contraseña incorrectos");
        }
    }

    public static class NoEncontradoException extends DAOException {

        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Class<?> clase;

        public NoEncontradoException(Class<?> clase) {
            super("No se encuentra el objeto " + clase.getName());
            this.clase = clase;
        }

        public boolean esClase(Class<?> clase) {
            return this.clase.equals(clase);
        }
    }

    public DAOException() {
        super("Error de acceso a datos");
    }

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }

    public DAOException(String message) {
        super(message);
    }

    public DAOException(Throwable cause) {
        super("Error de acceso a datos", cause);
    }

}

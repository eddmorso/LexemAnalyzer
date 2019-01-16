package PZKS.Opening;

public enum TypeOfToken {
    VARIABLE,
    CONSTANT,
    UNMINUS {
        public int priority() {
            return 1;
        }

        public int weight() {
            return 1;
        }
    },
    MINUS {
        public int priority() {
            return 1;
        }

        public int weight() {
            return 1;
        }
    },
    PLUS {
        public int priority() {
            return 1;
        }

        public int weight() {
            return 1;
        }
    },
    MULTIPLY {
        public int priority() {
            return 2;
        }

        public int weight() {
            return 1;
        }
    },
    DIVIDE {
        public int priority() {
            return 2;
        }

        public int weight() {
            return 1;
        }
    },
    OPENBRACKET,
    CLOSEBRACKET;

    public int priority() {
        return 0;
    }

    public int weight() {
        return 0;
    }
}



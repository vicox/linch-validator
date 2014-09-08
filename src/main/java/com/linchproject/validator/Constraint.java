package com.linchproject.validator;

/**
 * @author Georg Schmidl
 */
public interface Constraint {

    Result check(Value value);

    public class Result {

        private static Result OK = new Result(null);

        ErrorMessage errorMessage;

        protected Result(ErrorMessage errorMessage) {
            this.errorMessage = errorMessage;
        }

        public boolean isOk() {
            return errorMessage == null;
        }

        public ErrorMessage getErrorMessage() {
            return errorMessage;
        }

        public static Result ok() {
            return OK;
        }

        public static Result error(String messageKey) {
            return new Result(new ErrorMessage(messageKey));
        }

        public static Result error(String messageKey, Object... messageArguments) {
            return new Result(new ErrorMessage(messageKey, messageArguments));
        }
    }



}

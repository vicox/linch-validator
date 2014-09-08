package com.linchproject.validator;

/**
 * @author Georg Schmidl
 */
public interface Constraint {

    Result check(Value value);

    public class Result {

        private static Result OK = new Result(null);

        Error error;

        protected Result(Error error) {
            this.error = error;
        }

        public boolean isOk() {
            return error == null;
        }

        public Error getError() {
            return error;
        }

        public static Result ok() {
            return OK;
        }

        public static Result error(String messageKey) {
            return new Result(new Error(messageKey));
        }

        public static Result error(String messageKey, Object... messageArguments) {
            return new Result(new Error(messageKey, messageArguments));
        }
    }



}

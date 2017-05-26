export class UserCards {
    constructor(
        public id?: number,
        public creditCardNumber?: string,
        public lastUsageMonth?: string,
        public lastUsageYear?: string,
        public cvv?: string,
        public userId?: number,
        public toAccountId?: number,
    ) {
    }
}

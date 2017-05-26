export class AccountTransactions {
    constructor(
        public id?: number,
        public amount?: number,
        public fromUserEmailId?: number,
        public toUserEmailId?: number,
        public fromAccountId?: number,
        public toAccountId?: number,
    ) {
    }
}

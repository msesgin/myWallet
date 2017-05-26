import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { AccountTransactions } from './account-transactions.model';
import { AccountTransactionsService } from './account-transactions.service';

@Component({
    selector: 'jhi-account-transactions-detail',
    templateUrl: './account-transactions-detail.component.html'
})
export class AccountTransactionsDetailComponent implements OnInit, OnDestroy {

    accountTransactions: AccountTransactions;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private accountTransactionsService: AccountTransactionsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInAccountTransactions();
    }

    load(id) {
        this.accountTransactionsService.find(id).subscribe((accountTransactions) => {
            this.accountTransactions = accountTransactions;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAccountTransactions() {
        this.eventSubscriber = this.eventManager.subscribe(
            'accountTransactionsListModification',
            (response) => this.load(this.accountTransactions.id)
        );
    }
}

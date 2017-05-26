import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { CardTransactions } from './card-transactions.model';
import { CardTransactionsService } from './card-transactions.service';

@Component({
    selector: 'jhi-card-transactions-detail',
    templateUrl: './card-transactions-detail.component.html'
})
export class CardTransactionsDetailComponent implements OnInit, OnDestroy {

    cardTransactions: CardTransactions;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private cardTransactionsService: CardTransactionsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCardTransactions();
    }

    load(id) {
        this.cardTransactionsService.find(id).subscribe((cardTransactions) => {
            this.cardTransactions = cardTransactions;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCardTransactions() {
        this.eventSubscriber = this.eventManager.subscribe(
            'cardTransactionsListModification',
            (response) => this.load(this.cardTransactions.id)
        );
    }
}

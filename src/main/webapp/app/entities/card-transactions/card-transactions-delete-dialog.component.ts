import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { CardTransactions } from './card-transactions.model';
import { CardTransactionsPopupService } from './card-transactions-popup.service';
import { CardTransactionsService } from './card-transactions.service';

@Component({
    selector: 'jhi-card-transactions-delete-dialog',
    templateUrl: './card-transactions-delete-dialog.component.html'
})
export class CardTransactionsDeleteDialogComponent {

    cardTransactions: CardTransactions;

    constructor(
        private cardTransactionsService: CardTransactionsService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.cardTransactionsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'cardTransactionsListModification',
                content: 'Deleted an cardTransactions'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-card-transactions-delete-popup',
    template: ''
})
export class CardTransactionsDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cardTransactionsPopupService: CardTransactionsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.cardTransactionsPopupService
                .open(CardTransactionsDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

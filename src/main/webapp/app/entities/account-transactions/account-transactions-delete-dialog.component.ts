import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { AccountTransactions } from './account-transactions.model';
import { AccountTransactionsPopupService } from './account-transactions-popup.service';
import { AccountTransactionsService } from './account-transactions.service';

@Component({
    selector: 'jhi-account-transactions-delete-dialog',
    templateUrl: './account-transactions-delete-dialog.component.html'
})
export class AccountTransactionsDeleteDialogComponent {

    accountTransactions: AccountTransactions;

    constructor(
        private accountTransactionsService: AccountTransactionsService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.accountTransactionsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'accountTransactionsListModification',
                content: 'Deleted an accountTransactions'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-account-transactions-delete-popup',
    template: ''
})
export class AccountTransactionsDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private accountTransactionsPopupService: AccountTransactionsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.accountTransactionsPopupService
                .open(AccountTransactionsDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

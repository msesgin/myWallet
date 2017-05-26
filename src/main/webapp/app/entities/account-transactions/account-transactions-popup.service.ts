import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AccountTransactions } from './account-transactions.model';
import { AccountTransactionsService } from './account-transactions.service';
@Injectable()
export class AccountTransactionsPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private accountTransactionsService: AccountTransactionsService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.accountTransactionsService.find(id).subscribe((accountTransactions) => {
                this.accountTransactionsModalRef(component, accountTransactions);
            });
        } else {
            return this.accountTransactionsModalRef(component, new AccountTransactions());
        }
    }

    accountTransactionsModalRef(component: Component, accountTransactions: AccountTransactions): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.accountTransactions = accountTransactions;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}

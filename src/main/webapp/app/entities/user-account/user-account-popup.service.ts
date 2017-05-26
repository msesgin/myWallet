import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { UserAccount } from './user-account.model';
import { UserAccountService } from './user-account.service';
@Injectable()
export class UserAccountPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private userAccountService: UserAccountService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.userAccountService.find(id).subscribe((userAccount) => {
                this.userAccountModalRef(component, userAccount);
            });
        } else {
            return this.userAccountModalRef(component, new UserAccount());
        }
    }

    userAccountModalRef(component: Component, userAccount: UserAccount): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.userAccount = userAccount;
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

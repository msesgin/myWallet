import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { UserAccount } from './user-account.model';

@Injectable()
export class UserAccountService {

    private resourceUrl = 'api/user-accounts';
    private resourceUrlCurrentUser = 'api/user-accounts-current-user';
    private resourceUrlUser = 'api/user-accounts-user';

    constructor(private http: Http) { }

    create(userAccount: UserAccount): Observable<UserAccount> {
        const copy = this.convert(userAccount);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(userAccount: UserAccount): Observable<UserAccount> {
        const copy = this.convert(userAccount);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<UserAccount> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<Response> {
        const options = this.createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
        ;
    }
    
    queryByCurrentUser(req?: any): Observable<Response> {
        const options = this.createRequestOption(req);
        return this.http.get(this.resourceUrlCurrentUser, options)
        ;
    }
    
    queryByUser(userId: number): Observable<Response> {
        return this.http.get(`${this.resourceUrlUser}/${userId}`).map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: any): any {
        let jsonResponse = res.json();
        res._body = jsonResponse;
        return res;
    }
    
    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }
    private createRequestOption(req?: any): BaseRequestOptions {
        const options: BaseRequestOptions = new BaseRequestOptions();
        if (req) {
            const params: URLSearchParams = new URLSearchParams();
            params.set('page', req.page);
            params.set('size', req.size);
            if (req.sort) {
                params.paramsMap.set('sort', req.sort);
            }
            params.set('query', req.query);

            options.search = params;
        }
        return options;
    }

    private convert(userAccount: UserAccount): UserAccount {
        const copy: UserAccount = Object.assign({}, userAccount);
        return copy;
    }
}

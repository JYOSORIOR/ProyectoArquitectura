import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IATM, NewATM } from '../atm.model';

export type PartialUpdateATM = Partial<IATM> & Pick<IATM, 'id'>;

export type EntityResponseType = HttpResponse<IATM>;
export type EntityArrayResponseType = HttpResponse<IATM[]>;

@Injectable({ providedIn: 'root' })
export class ATMService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/atms', 'backend');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(aTM: NewATM): Observable<EntityResponseType> {
    return this.http.post<IATM>(this.resourceUrl, aTM, { observe: 'response' });
  }

  update(aTM: IATM): Observable<EntityResponseType> {
    return this.http.put<IATM>(`${this.resourceUrl}/${this.getATMIdentifier(aTM)}`, aTM, { observe: 'response' });
  }

  partialUpdate(aTM: PartialUpdateATM): Observable<EntityResponseType> {
    return this.http.patch<IATM>(`${this.resourceUrl}/${this.getATMIdentifier(aTM)}`, aTM, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IATM>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IATM[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getATMIdentifier(aTM: Pick<IATM, 'id'>): number {
    return aTM.id;
  }

  compareATM(o1: Pick<IATM, 'id'> | null, o2: Pick<IATM, 'id'> | null): boolean {
    return o1 && o2 ? this.getATMIdentifier(o1) === this.getATMIdentifier(o2) : o1 === o2;
  }

  addATMToCollectionIfMissing<Type extends Pick<IATM, 'id'>>(aTMCollection: Type[], ...aTMSToCheck: (Type | null | undefined)[]): Type[] {
    const aTMS: Type[] = aTMSToCheck.filter(isPresent);
    if (aTMS.length > 0) {
      const aTMCollectionIdentifiers = aTMCollection.map(aTMItem => this.getATMIdentifier(aTMItem)!);
      const aTMSToAdd = aTMS.filter(aTMItem => {
        const aTMIdentifier = this.getATMIdentifier(aTMItem);
        if (aTMCollectionIdentifiers.includes(aTMIdentifier)) {
          return false;
        }
        aTMCollectionIdentifiers.push(aTMIdentifier);
        return true;
      });
      return [...aTMSToAdd, ...aTMCollection];
    }
    return aTMCollection;
  }
}

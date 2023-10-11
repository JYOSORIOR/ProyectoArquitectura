import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRetiro, NewRetiro } from '../retiro.model';

export type PartialUpdateRetiro = Partial<IRetiro> & Pick<IRetiro, 'id'>;

type RestOf<T extends IRetiro | NewRetiro> = Omit<T, 'fecha'> & {
  fecha?: string | null;
};

export type RestRetiro = RestOf<IRetiro>;

export type NewRestRetiro = RestOf<NewRetiro>;

export type PartialUpdateRestRetiro = RestOf<PartialUpdateRetiro>;

export type EntityResponseType = HttpResponse<IRetiro>;
export type EntityArrayResponseType = HttpResponse<IRetiro[]>;

@Injectable({ providedIn: 'root' })
export class RetiroService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/retiros', 'backend');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(retiro: NewRetiro): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(retiro);
    return this.http
      .post<RestRetiro>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(retiro: IRetiro): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(retiro);
    return this.http
      .put<RestRetiro>(`${this.resourceUrl}/${this.getRetiroIdentifier(retiro)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(retiro: PartialUpdateRetiro): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(retiro);
    return this.http
      .patch<RestRetiro>(`${this.resourceUrl}/${this.getRetiroIdentifier(retiro)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestRetiro>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRetiro[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRetiroIdentifier(retiro: Pick<IRetiro, 'id'>): number {
    return retiro.id;
  }

  compareRetiro(o1: Pick<IRetiro, 'id'> | null, o2: Pick<IRetiro, 'id'> | null): boolean {
    return o1 && o2 ? this.getRetiroIdentifier(o1) === this.getRetiroIdentifier(o2) : o1 === o2;
  }

  addRetiroToCollectionIfMissing<Type extends Pick<IRetiro, 'id'>>(
    retiroCollection: Type[],
    ...retirosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const retiros: Type[] = retirosToCheck.filter(isPresent);
    if (retiros.length > 0) {
      const retiroCollectionIdentifiers = retiroCollection.map(retiroItem => this.getRetiroIdentifier(retiroItem)!);
      const retirosToAdd = retiros.filter(retiroItem => {
        const retiroIdentifier = this.getRetiroIdentifier(retiroItem);
        if (retiroCollectionIdentifiers.includes(retiroIdentifier)) {
          return false;
        }
        retiroCollectionIdentifiers.push(retiroIdentifier);
        return true;
      });
      return [...retirosToAdd, ...retiroCollection];
    }
    return retiroCollection;
  }

  protected convertDateFromClient<T extends IRetiro | NewRetiro | PartialUpdateRetiro>(retiro: T): RestOf<T> {
    return {
      ...retiro,
      fecha: retiro.fecha?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restRetiro: RestRetiro): IRetiro {
    return {
      ...restRetiro,
      fecha: restRetiro.fecha ? dayjs(restRetiro.fecha) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestRetiro>): HttpResponse<IRetiro> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestRetiro[]>): HttpResponse<IRetiro[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}

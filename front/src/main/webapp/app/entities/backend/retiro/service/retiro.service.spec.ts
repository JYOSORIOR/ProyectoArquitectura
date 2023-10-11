import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRetiro } from '../retiro.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../retiro.test-samples';

import { RetiroService, RestRetiro } from './retiro.service';

const requireRestSample: RestRetiro = {
  ...sampleWithRequiredData,
  fecha: sampleWithRequiredData.fecha?.toJSON(),
};

describe('Retiro Service', () => {
  let service: RetiroService;
  let httpMock: HttpTestingController;
  let expectedResult: IRetiro | IRetiro[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RetiroService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Retiro', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const retiro = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(retiro).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Retiro', () => {
      const retiro = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(retiro).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Retiro', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Retiro', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Retiro', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRetiroToCollectionIfMissing', () => {
      it('should add a Retiro to an empty array', () => {
        const retiro: IRetiro = sampleWithRequiredData;
        expectedResult = service.addRetiroToCollectionIfMissing([], retiro);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(retiro);
      });

      it('should not add a Retiro to an array that contains it', () => {
        const retiro: IRetiro = sampleWithRequiredData;
        const retiroCollection: IRetiro[] = [
          {
            ...retiro,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRetiroToCollectionIfMissing(retiroCollection, retiro);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Retiro to an array that doesn't contain it", () => {
        const retiro: IRetiro = sampleWithRequiredData;
        const retiroCollection: IRetiro[] = [sampleWithPartialData];
        expectedResult = service.addRetiroToCollectionIfMissing(retiroCollection, retiro);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(retiro);
      });

      it('should add only unique Retiro to an array', () => {
        const retiroArray: IRetiro[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const retiroCollection: IRetiro[] = [sampleWithRequiredData];
        expectedResult = service.addRetiroToCollectionIfMissing(retiroCollection, ...retiroArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const retiro: IRetiro = sampleWithRequiredData;
        const retiro2: IRetiro = sampleWithPartialData;
        expectedResult = service.addRetiroToCollectionIfMissing([], retiro, retiro2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(retiro);
        expect(expectedResult).toContain(retiro2);
      });

      it('should accept null and undefined values', () => {
        const retiro: IRetiro = sampleWithRequiredData;
        expectedResult = service.addRetiroToCollectionIfMissing([], null, retiro, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(retiro);
      });

      it('should return initial array if no Retiro is added', () => {
        const retiroCollection: IRetiro[] = [sampleWithRequiredData];
        expectedResult = service.addRetiroToCollectionIfMissing(retiroCollection, undefined, null);
        expect(expectedResult).toEqual(retiroCollection);
      });
    });

    describe('compareRetiro', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRetiro(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRetiro(entity1, entity2);
        const compareResult2 = service.compareRetiro(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRetiro(entity1, entity2);
        const compareResult2 = service.compareRetiro(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRetiro(entity1, entity2);
        const compareResult2 = service.compareRetiro(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

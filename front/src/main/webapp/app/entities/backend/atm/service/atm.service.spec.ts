import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IATM } from '../atm.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../atm.test-samples';

import { ATMService } from './atm.service';

const requireRestSample: IATM = {
  ...sampleWithRequiredData,
};

describe('ATM Service', () => {
  let service: ATMService;
  let httpMock: HttpTestingController;
  let expectedResult: IATM | IATM[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ATMService);
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

    it('should create a ATM', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const aTM = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(aTM).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ATM', () => {
      const aTM = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(aTM).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ATM', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ATM', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ATM', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addATMToCollectionIfMissing', () => {
      it('should add a ATM to an empty array', () => {
        const aTM: IATM = sampleWithRequiredData;
        expectedResult = service.addATMToCollectionIfMissing([], aTM);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aTM);
      });

      it('should not add a ATM to an array that contains it', () => {
        const aTM: IATM = sampleWithRequiredData;
        const aTMCollection: IATM[] = [
          {
            ...aTM,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addATMToCollectionIfMissing(aTMCollection, aTM);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ATM to an array that doesn't contain it", () => {
        const aTM: IATM = sampleWithRequiredData;
        const aTMCollection: IATM[] = [sampleWithPartialData];
        expectedResult = service.addATMToCollectionIfMissing(aTMCollection, aTM);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aTM);
      });

      it('should add only unique ATM to an array', () => {
        const aTMArray: IATM[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const aTMCollection: IATM[] = [sampleWithRequiredData];
        expectedResult = service.addATMToCollectionIfMissing(aTMCollection, ...aTMArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const aTM: IATM = sampleWithRequiredData;
        const aTM2: IATM = sampleWithPartialData;
        expectedResult = service.addATMToCollectionIfMissing([], aTM, aTM2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aTM);
        expect(expectedResult).toContain(aTM2);
      });

      it('should accept null and undefined values', () => {
        const aTM: IATM = sampleWithRequiredData;
        expectedResult = service.addATMToCollectionIfMissing([], null, aTM, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aTM);
      });

      it('should return initial array if no ATM is added', () => {
        const aTMCollection: IATM[] = [sampleWithRequiredData];
        expectedResult = service.addATMToCollectionIfMissing(aTMCollection, undefined, null);
        expect(expectedResult).toEqual(aTMCollection);
      });
    });

    describe('compareATM', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareATM(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareATM(entity1, entity2);
        const compareResult2 = service.compareATM(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareATM(entity1, entity2);
        const compareResult2 = service.compareATM(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareATM(entity1, entity2);
        const compareResult2 = service.compareATM(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

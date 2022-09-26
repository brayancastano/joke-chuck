import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IJokesUser } from '../jokes-user.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../jokes-user.test-samples';

import { JokesUserService } from './jokes-user.service';

const requireRestSample: IJokesUser = {
  ...sampleWithRequiredData,
};

describe('JokesUser Service', () => {
  let service: JokesUserService;
  let httpMock: HttpTestingController;
  let expectedResult: IJokesUser | IJokesUser[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(JokesUserService);
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

    it('should create a JokesUser', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const jokesUser = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(jokesUser).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a JokesUser', () => {
      const jokesUser = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(jokesUser).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a JokesUser', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of JokesUser', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a JokesUser', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addJokesUserToCollectionIfMissing', () => {
      it('should add a JokesUser to an empty array', () => {
        const jokesUser: IJokesUser = sampleWithRequiredData;
        expectedResult = service.addJokesUserToCollectionIfMissing([], jokesUser);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(jokesUser);
      });

      it('should not add a JokesUser to an array that contains it', () => {
        const jokesUser: IJokesUser = sampleWithRequiredData;
        const jokesUserCollection: IJokesUser[] = [
          {
            ...jokesUser,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addJokesUserToCollectionIfMissing(jokesUserCollection, jokesUser);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a JokesUser to an array that doesn't contain it", () => {
        const jokesUser: IJokesUser = sampleWithRequiredData;
        const jokesUserCollection: IJokesUser[] = [sampleWithPartialData];
        expectedResult = service.addJokesUserToCollectionIfMissing(jokesUserCollection, jokesUser);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(jokesUser);
      });

      it('should add only unique JokesUser to an array', () => {
        const jokesUserArray: IJokesUser[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const jokesUserCollection: IJokesUser[] = [sampleWithRequiredData];
        expectedResult = service.addJokesUserToCollectionIfMissing(jokesUserCollection, ...jokesUserArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const jokesUser: IJokesUser = sampleWithRequiredData;
        const jokesUser2: IJokesUser = sampleWithPartialData;
        expectedResult = service.addJokesUserToCollectionIfMissing([], jokesUser, jokesUser2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(jokesUser);
        expect(expectedResult).toContain(jokesUser2);
      });

      it('should accept null and undefined values', () => {
        const jokesUser: IJokesUser = sampleWithRequiredData;
        expectedResult = service.addJokesUserToCollectionIfMissing([], null, jokesUser, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(jokesUser);
      });

      it('should return initial array if no JokesUser is added', () => {
        const jokesUserCollection: IJokesUser[] = [sampleWithRequiredData];
        expectedResult = service.addJokesUserToCollectionIfMissing(jokesUserCollection, undefined, null);
        expect(expectedResult).toEqual(jokesUserCollection);
      });
    });

    describe('compareJokesUser', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareJokesUser(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareJokesUser(entity1, entity2);
        const compareResult2 = service.compareJokesUser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareJokesUser(entity1, entity2);
        const compareResult2 = service.compareJokesUser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareJokesUser(entity1, entity2);
        const compareResult2 = service.compareJokesUser(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

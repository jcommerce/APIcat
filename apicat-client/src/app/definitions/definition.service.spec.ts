import {async, inject, TestBed} from '@angular/core/testing';
import {BaseRequestOptions, Http, HttpModule, Response, ResponseOptions, RequestMethod} from '@angular/http';
import {MockBackend} from '@angular/http/testing';
import {DefinitionService} from './definition.service';

const mockDefinition = {
  id: 1,
  name: 'apicat api',
  version: '2.3.5',
  author: 'John Doe',
  format: 'SWAGGER',
  content: 'foo bar'
};

describe('DefinitionService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        DefinitionService
      ],
      imports: [
        HttpModule
      ]
    });
  });

  it('should construct', async(inject([DefinitionService], (service) => {
    expect(service).toBeDefined();
  })));
});

describe('DefinitionService (Mocked)', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        DefinitionService,
        MockBackend,
        BaseRequestOptions,
        {
          provide: Http,
          useFactory: (backend, options) => new Http(backend, options),
          deps: [MockBackend, BaseRequestOptions]
        }
      ],
      imports: [
        HttpModule
      ]
    });
  });

  it('should construct', async(inject([DefinitionService, MockBackend], (service, mockBackend) => {
    expect(service).toBeDefined();
  })));

  describe('getAll', () => {
    const mockResponse = {
      data: [mockDefinition]
    };

    it('should call backend', async(inject(
      [DefinitionService, MockBackend], (service, mockBackend) => {

        mockBackend.connections.subscribe(conn => {
          expect(conn.request.method).toBe(RequestMethod.Get);
          expect(conn.request.url).toBe('/api/definitions');
        });

        service.getAll();
      })));

    it('should parse response', async(inject(
      [DefinitionService, MockBackend], (service, mockBackend) => {

        mockBackend.connections.subscribe(conn => {
          conn.mockRespond(new Response(new ResponseOptions({body: JSON.stringify(mockResponse)})));
        });

        const result = service.getAll();

        result.subscribe(res => expect(res).toEqual(mockResponse.data));
      })));
  });

  describe('getOne', () => {
    const mockResponse = {
      data: mockDefinition
    };

    it('should call backend', async(inject(
      [DefinitionService, MockBackend], (service, mockBackend) => {

        mockBackend.connections.subscribe(conn => {
          expect(conn.request.method).toBe(RequestMethod.Get);
          expect(conn.request.url).toBe('/api/definitions/' + mockDefinition.id);
        });

        service.getOne(mockDefinition.id);
      })));

    it('should parse response', async(inject(
      [DefinitionService, MockBackend], (service, mockBackend) => {

        mockBackend.connections.subscribe(conn => {
          conn.mockRespond(new Response(new ResponseOptions({body: JSON.stringify(mockResponse)})));
        });

        service.getOne(mockDefinition.id).subscribe(res =>
          expect(res).toEqual(mockDefinition)
        );
      })));
  });

  describe('create', () => {
    const mockResponse = {
      data: mockDefinition
    };

    it('should call backend', async(inject(
      [DefinitionService, MockBackend], (service, mockBackend) => {

        mockBackend.connections.subscribe(conn => {
          expect(conn.request.method).toBe(RequestMethod.Post);
          expect(conn.request.url).toBe('/api/definitions');
          expect(conn.request.getBody()).toBe(JSON.stringify(mockDefinition));
        });

        service.create(mockDefinition);
      })));

    it('should parse response', async(inject(
      [DefinitionService, MockBackend], (service, mockBackend) => {

        mockBackend.connections.subscribe(conn => {
          conn.mockRespond(new Response(new ResponseOptions({body: JSON.stringify(mockResponse)})));
        });

        service.create(mockDefinition).subscribe(res =>
          expect(res).toEqual(mockDefinition)
        );
      })));
  });

  describe('update', () => {
    const mockResponse = {
      data: mockDefinition
    };

    it('should call backend', async(inject(
      [DefinitionService, MockBackend], (service, mockBackend) => {

        mockBackend.connections.subscribe(conn => {
          expect(conn.request.method).toBe(RequestMethod.Put);
          expect(conn.request.url).toBe('/api/definitions/' + mockDefinition.id);
          expect(conn.request.getBody()).toBe(JSON.stringify(mockDefinition));
        });

        service.update(mockDefinition);
      })));

    it('should parse response', async(inject(
      [DefinitionService, MockBackend], (service, mockBackend) => {

        mockBackend.connections.subscribe(conn => {
          conn.mockRespond(new Response(new ResponseOptions({body: JSON.stringify(mockResponse)})));
        });

        service.update(mockDefinition).subscribe(res =>
          expect(res).toEqual(mockDefinition)
        );
      })));
  });

  describe('delete', () => {
    it('should call backend', async(inject(
      [DefinitionService, MockBackend], (service, mockBackend) => {

        mockBackend.connections.subscribe(conn => {
          expect(conn.request.method).toBe(RequestMethod.Delete);
          expect(conn.request.url).toBe('/api/definitions/' + mockDefinition.id);
        });

        service.delete(mockDefinition.id);
      })));
  });
});

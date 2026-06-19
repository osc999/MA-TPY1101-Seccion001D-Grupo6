import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = 'https://ma-tpy1101-seccion001d-grupo6-production-5653.up.railway.app/';

  constructor(private http: HttpClient) {}

  login(email: string, password: string) {
    return this.http.post(`${this.apiUrl}api/auth/login`, {
      email,
      password
    });
  }
  
  register(nombre: string, email: string, password: string) {
    return this.http.post(`${this.apiUrl}api/usuarios`, {
      nombre,
      email,
      password
    });
  }
}
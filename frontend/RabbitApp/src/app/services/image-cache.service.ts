import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root', // Omogućava globalnu dostupnost servisa u aplikaciji.
})
export class ImageCacheService {
  private cache = new Map<string, string>(); // Map za keširanje slika. Ključ je URL slike, a vrednost je keširani "data URL".

  /**
   * Preuzima sliku sa zadatog URL-a i čuva je u kešu ako nije već keširana.
   * 
   * @param url URL slike koju treba preuzeti ili dohvatiti iz keša.
   * @returns `Promise<string>` koji sadrži "data URL" slike.
   */
  async fetchImage(url: string): Promise<string> {
    // Proverava da li je URL već u kešu.
    if (this.cache.has(url)) {
      return this.cache.get(url)!; // Vraća keširanu sliku ako postoji.
    }

    // Preuzima sliku sa servera.
    const response = await fetch(url);
    const blob = await response.blob(); // Konvertuje odgovor u `blob` format.

    // Kreira "data URL" iz `blob` objekta.
    const dataUrl = URL.createObjectURL(blob);

    // Dodaje kreirani "data URL" u keš.
    this.cache.set(url, dataUrl);

    // Vraća "data URL" slike.
    return dataUrl;
  }
}

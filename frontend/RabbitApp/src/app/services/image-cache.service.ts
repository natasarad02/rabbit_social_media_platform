import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root', 
})
export class ImageCacheService {
  // Naziv keša koji će se koristiti u Cache API-ju.
  private readonly CACHE_NAME = 'image-cache-v1';

  /**
   * Preuzima sliku sa zadatog URL-a i čuva je u trajnom kešu (Cache API).
   * Ako slika postoji u kešu, vraća je odatle i ne preuzima je ponovo sa servera.
   *
   * @param url URL slike koju treba preuzeti ili dohvatiti iz keša.
   * @returns `Promise<string>` koji sadrži "Blob URL" slike.
   */
  async fetchImage(url: string): Promise<string> {
    const cache = await caches.open(this.CACHE_NAME);
    const cachedResponse = await cache.match(url);

    if (cachedResponse) {
      // OVAJ DEO SE IZVRŠAVA NAKON REFRESH-A
      console.log(`CACHE HIT: Slika ${url} je učitana iz keša.`);
      const blob = await cachedResponse.blob();
      return URL.createObjectURL(blob);
    }

    // OVAJ DEO SE IZVRŠAVA SAMO PRVI PUT
    console.log(`CACHE MISS: Slika ${url} se preuzima sa servera.`);
    const response = await fetch(url);
    if (response.ok) {
        await cache.put(url, response.clone());
    }
    const blob = await response.blob();
    return URL.createObjectURL(blob);
}
}

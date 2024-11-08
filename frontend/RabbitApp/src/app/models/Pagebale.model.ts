export interface PaginatedResponse<T> {
    content: T[];
    pageable: {
        pageNumber: number;
        pageSize: number;
        
    };
    totalElements: number;
    totalPages: number;
    numberOfElements: number;
    first: boolean;
    last: boolean;
    
}

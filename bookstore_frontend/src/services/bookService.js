// eslint-disable-next-line no-unused-vars
import {postRequest} from "../utils/ajax";

export function getBooks(page, sortId, callback){
    const url = 'http://localhost:8080/getBooks';
    const data = {page: page, sortId: sortId};
    postRequest(url, data, callback);
};

export function searchBooks(page, bookName, callback){
    const url = 'http://localhost:8080/searchBooks';
    const data = {page: page, bookName: bookName};
    postRequest(url, data, callback);
};

export function getBook(id, callback) {
    const data = { id: id };
    const url = 'http://localhost:8080/getBookById';
    console.log(data);
    postRequest(url, data, callback);
};

export function saveBook (dataSrc, callback){
    const url = 'http://localhost:8080/saveBook';
    const data = {
        bookId: dataSrc.bookId,
        isbn: dataSrc.isbn,
        name: dataSrc.name,
        author: dataSrc.author,
        description: dataSrc.description,
        image: dataSrc.image,
        inventory: dataSrc.inventory,
        price: dataSrc.price,
        type: dataSrc.type,
    }
    postRequest(url, data, callback);
}

export function deleteBookById (Id, callback){
    const url = 'http://localhost:8080/deleteBookById';
    const data ={
        bookId: Id,
    };
    postRequest(url, data, callback);
}

export function addComment (Id, text, callback){

    const url = 'http://localhost:8080/addComment';
    const data ={
        bookId: Id,
        comment: text
    };
    console.log(data)
    postRequest(url, data, callback);
}


export function addBook (callback) {
    const url = 'http://localhost:8080/addBook';
    postRequest(url, {}, callback);
}


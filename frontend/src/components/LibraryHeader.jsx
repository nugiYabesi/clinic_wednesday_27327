import React from 'react'
import { Link } from 'react-router'

function LibraryHeader() {

//     const libraryHeader = "LIBRARY MANAGEMENT SYSTEM";
    



//     const books = [
//         { CODE: "B001", TITLE: "The Great Gatsby", AUTHOR: "F. Scott Fitzgerald", PRICE: 40 },
//         { CODE: "B002", TITLE: "To Kill a Mockingbird", AUTHOR: "Harper Lee", PRICE: 50 },
//         { CODE: "B003", TITLE: "1984", AUTHOR: "George Orwell", PRICE: 60 }
//     ];
//     const totalPrice = books.reduce((total, book) => total + book.PRICE, 0);

//   return (
//     <div> 
//         <div>
//             <h1>{libraryHeader}</h1>
//         </div>

//         <div><h2>The list of available books</h2></div>

//         <div>
//             <ul>



//                 {books.map((book) => (<li>{book.TITLE} by {book.AUTHOR}</li>))}
//             </ul>






//         </div>
//         <div>The total price for {books.length} books is ${totalPrice.toFixed(2)}</div>
//     </div>
//   )

return (
    
    <nav>
        
    <Link to="/" style={{paddingRight:"10px"}}>Dashboard</Link>
    <Link to="/office" style={{paddingRight:"10px"}}>Office</Link>
    <Link to="/main" style={{paddingRight:"10px"}}>Main</Link>
</nav>

)
}

export default LibraryHeader

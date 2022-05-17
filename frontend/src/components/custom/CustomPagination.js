import React, { } from 'react';
import { Pagination, PaginationItem, PaginationLink } from 'reactstrap';

const paginationLimit = 7;

function range(start, end) {
    let numsList = [];

    for(let num = start; num <= end; num++) {
        numsList.push(num);
    }

    return numsList;
}

function CustomPagination(props) {

    function generatePaginationList() {
        const totalPages = props.totalPages;
        const currentPage = props.currentPage;

        const firstPage = 1;
        const lastPage = totalPages;

        // Show all page numbers
        if(totalPages < paginationLimit) {
            return range(firstPage, lastPage);
        }

        const distanceToFirstPage = currentPage - firstPage;
        const distanceToLastPage = lastPage - currentPage;

        const [requireLeftDots, requireRightDots] = [distanceToFirstPage > 2, distanceToLastPage > 2];

        // Current page near start
        if(!requireLeftDots && requireRightDots) {
            return [...range(firstPage, firstPage + 4), "...", lastPage];
        }

        // Current page near middle
        if(requireLeftDots && requireRightDots) {
            return [firstPage, "...", range(currentPage - 1, currentPage + 1), "...", lastPage];
        }

        // Current page near end
        if(requireLeftDots && !requireRightDots) {
            return [firstPage, "...", range(lastPage - 4, lastPage)]
        }    
    }

    let paginationTexts = generatePaginationList();

    let paginationItems = paginationTexts.map((text) => {
        if (text === "...") {
            return (
                <PaginationItem disabled>
                    <PaginationLink>
                        {text}
                    </PaginationLink>
                </PaginationItem>
            );
        }

        return (
            <PaginationItem active={props.currentPage === parseInt(text)}>
                <PaginationLink onClick={() => props.switchPage(parseInt(text))}>
                    {text}
                </PaginationLink>
            </PaginationItem>
        );
    })

    return (
        <Pagination>
            <PaginationItem>
                <PaginationLink first onClick={() => props.switchPage(1)} />
            </PaginationItem>
            <PaginationItem>
                <PaginationLink previous onClick={() => props.switchPage(Math.max(props.currentPage - 1, 1))} />
            </PaginationItem>

            {paginationItems}

            <PaginationItem>
                <PaginationLink next onClick={() => props.switchPage(Math.min(props.currentPage + 1, Math.max(props.totalPages, 1)))} />
            </PaginationItem>
            <PaginationItem>
                <PaginationLink last onClick={() => props.switchPage(Math.max(props.totalPages, 1))} />
            </PaginationItem>
        </Pagination>
    );
}

export default CustomPagination;
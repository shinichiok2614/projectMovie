import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { openFile, byteSize, Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './bap-nuoc.reducer';

export const BapNuoc = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const bapNuocList = useAppSelector(state => state.bapNuoc.entities);
  const loading = useAppSelector(state => state.bapNuoc.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="bap-nuoc-heading" data-cy="BapNuocHeading">
        <Translate contentKey="projectMovieApp.bapNuoc.home.title">Bap Nuocs</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="projectMovieApp.bapNuoc.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/bap-nuoc/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="projectMovieApp.bapNuoc.home.createLabel">Create new Bap Nuoc</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {bapNuocList && bapNuocList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="projectMovieApp.bapNuoc.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('tenBapNuoc')}>
                  <Translate contentKey="projectMovieApp.bapNuoc.tenBapNuoc">Ten Bap Nuoc</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('tenBapNuoc')} />
                </th>
                <th className="hand" onClick={sort('logo')}>
                  <Translate contentKey="projectMovieApp.bapNuoc.logo">Logo</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('logo')} />
                </th>
                <th className="hand" onClick={sort('giaTien')}>
                  <Translate contentKey="projectMovieApp.bapNuoc.giaTien">Gia Tien</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('giaTien')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {bapNuocList.map((bapNuoc, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/bap-nuoc/${bapNuoc.id}`} color="link" size="sm">
                      {bapNuoc.id}
                    </Button>
                  </td>
                  <td>{bapNuoc.tenBapNuoc}</td>
                  <td>
                    {bapNuoc.logo ? (
                      <div>
                        {bapNuoc.logoContentType ? (
                          <a onClick={openFile(bapNuoc.logoContentType, bapNuoc.logo)}>
                            <img src={`data:${bapNuoc.logoContentType};base64,${bapNuoc.logo}`} style={{ maxHeight: '30px' }} />
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {bapNuoc.logoContentType}, {byteSize(bapNuoc.logo)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{bapNuoc.giaTien}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/bap-nuoc/${bapNuoc.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/bap-nuoc/${bapNuoc.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/bap-nuoc/${bapNuoc.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="projectMovieApp.bapNuoc.home.notFound">No Bap Nuocs found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default BapNuoc;

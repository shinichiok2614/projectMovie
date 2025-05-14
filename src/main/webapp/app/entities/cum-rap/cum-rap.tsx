import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { openFile, byteSize, Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './cum-rap.reducer';

export const CumRap = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const cumRapList = useAppSelector(state => state.cumRap.entities);
  const loading = useAppSelector(state => state.cumRap.loading);

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
      <h2 id="cum-rap-heading" data-cy="CumRapHeading">
        <Translate contentKey="projectMovieApp.cumRap.home.title">Cum Raps</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="projectMovieApp.cumRap.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/cum-rap/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="projectMovieApp.cumRap.home.createLabel">Create new Cum Rap</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {cumRapList && cumRapList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="projectMovieApp.cumRap.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('tenCumRap')}>
                  <Translate contentKey="projectMovieApp.cumRap.tenCumRap">Ten Cum Rap</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('tenCumRap')} />
                </th>
                <th className="hand" onClick={sort('logo')}>
                  <Translate contentKey="projectMovieApp.cumRap.logo">Logo</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('logo')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {cumRapList.map((cumRap, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/cum-rap/${cumRap.id}`} color="link" size="sm">
                      {cumRap.id}
                    </Button>
                  </td>
                  <td>{cumRap.tenCumRap}</td>
                  <td>
                    {cumRap.logo ? (
                      <div>
                        {cumRap.logoContentType ? (
                          <a onClick={openFile(cumRap.logoContentType, cumRap.logo)}>
                            <img src={`data:${cumRap.logoContentType};base64,${cumRap.logo}`} style={{ maxHeight: '30px' }} />
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {cumRap.logoContentType}, {byteSize(cumRap.logo)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/cum-rap/${cumRap.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/cum-rap/${cumRap.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/cum-rap/${cumRap.id}/delete`)}
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
              <Translate contentKey="projectMovieApp.cumRap.home.notFound">No Cum Raps found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default CumRap;
